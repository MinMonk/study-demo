/**
 * 
 * 文件名：ProxyService.java
 * 版权： Copyright 2017-2022 CMCC All Rights Reserved.
 * 描述： ESB管理系统
 */
package com.monk.controller;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.net.URI;
import java.util.Base64;
import java.util.BitSet;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.Base64.Encoder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.AbortableHttpRequest;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.HeaderGroup;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2021年7月15日 上午11:08:51
 */
public class ProxyService {

    private static final Logger logger = LoggerFactory.getLogger(ProxyService.class);

    private String targetUri;

    private boolean doLog = true;
    private URI targetUriObj = null;
    private HttpHost targetHost = null;
    private int connectTimeout = -1;
    private int readTimeout = -1;
    private int connectionRequestTimeout = -1;
    private int maxConnections = -1;
    private String servletName = "customProxyServlet";
    private HttpClient proxyClient;

    private boolean doForwardIP = true;
    private boolean doHandleCompression = false;
    private boolean useSystemProperties = true;
    private boolean doSendUrlFragment = true;
    private boolean doPreserveHost = false;
    private boolean doPreserveCookies = false;
    private boolean doHandleRedirects = false;
    private static final String ATTR_TARGET_URI = "targetUri";
    private static final String ATTR_TARGET_HOST = "targetHost";
    protected static final HeaderGroup hopByHopHeaders = new HeaderGroup();

    private ProxyService(String targetUri) throws ServletException {
        this.targetUri = targetUri;

        initTargetConfig();
        proxyClient = createHttpClient();
        initHeader();
    }

    public static ProxyService getProxyService(String targetUri) throws ServletException {
        return new ProxyService(targetUri);
    }

    /**
     * 初始化Header参数
     * 
     * @author Monk
     * @date 2021年7月15日 上午11:47:53
     */
    private void initHeader() {
        String[] headers = new String[] { "Connection", "Keep-Alive", "Proxy-Authenticate", "Proxy-Authorization",
                "TE", "Trailers", "Transfer-Encoding", "Upgrade" };
        for (String header : headers) {
            hopByHopHeaders.addHeader(new BasicHeader(header, null));
        }

    }

    /**
     * 初始化代理目标地址
     * 
     * @throws ServletException
     * @author Monk
     * @date 2021年7月15日 下午2:46:19
     */
    private void initTargetConfig() throws ServletException {
        if (targetUri == null) {
            throw new ServletException("targetUri is required.");
        }
        try {
            targetUriObj = new URI(targetUri);
        } catch (Exception e) {
            throw new ServletException("Trying to process targetUri init parameter: " + e, e);
        }
        targetHost = URIUtils.extractHost(targetUriObj);
    }

    /**
     * 创建HttpClient
     * 
     * @return Http客户端
     * @author Monk
     * @date 2021年7月15日 下午2:44:27
     */
    private HttpClient createHttpClient() {
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        clientBuilder.setDefaultRequestConfig(buildRequestConfig());
        clientBuilder.setDefaultSocketConfig(buildSocketConfig());
        clientBuilder.setMaxConnTotal(maxConnections);
        clientBuilder.setMaxConnPerRoute(maxConnections);
        if (!doHandleCompression) {
            clientBuilder.disableContentCompression();
        }

        if (useSystemProperties) {
            clientBuilder = clientBuilder.useSystemProperties();
        }

        return clientBuilder.build();
    }

    /**
     * 构建RequestConfig
     * 
     * @return 请求配置
     * @author Monk
     * @date 2021年7月15日 下午2:45:07
     */
    private RequestConfig buildRequestConfig() {
        return RequestConfig.custom().setRedirectsEnabled(doHandleRedirects)
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES).setConnectTimeout(connectTimeout)
                .setSocketTimeout(readTimeout).setConnectionRequestTimeout(connectionRequestTimeout).build();
    }

    /**
     * 构建Socket连接配置
     * 
     * @return Socket连接配置
     * @author Monk
     * @date 2021年7月15日 下午2:45:31
     */
    private SocketConfig buildSocketConfig() {
        if (readTimeout < 1) {
            return null;
        }

        return SocketConfig.custom().setSoTimeout(readTimeout).build();
    }

    /**
     * 代理
     * 
     * @param servletRequest
     *            servlet请求对象
     * @param servletResponse
     *            servlet响应对象
     * @throws IOException
     *             IO异常
     * @throws ServletException
     *             Servlet异常
     * @author Monk
     * @date 2021年7月15日 下午2:48:32
     */
    public void proxy(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
            throws IOException, ServletException {
        if (servletRequest.getAttribute(ATTR_TARGET_URI) == null) {
            servletRequest.setAttribute(ATTR_TARGET_URI, targetUri);
        }
        if (servletRequest.getAttribute(ATTR_TARGET_HOST) == null) {
            servletRequest.setAttribute(ATTR_TARGET_HOST, targetHost);
        }

        String method = servletRequest.getMethod();
        String proxyRequestUri = rewriteUrlFromRequest(servletRequest);
        HttpRequest proxyRequest;
        if (servletRequest.getHeader(HttpHeaders.CONTENT_LENGTH) != null
                || servletRequest.getHeader(HttpHeaders.TRANSFER_ENCODING) != null) {
            proxyRequest = newProxyRequestWithEntity(method, proxyRequestUri, servletRequest);
        } else {
            proxyRequest = new BasicHttpRequest(method, proxyRequestUri);
        }

        copyRequestHeaders(servletRequest, proxyRequest);

        setXForwardedForHeader(servletRequest, proxyRequest);

        HttpResponse proxyResponse = null;

        try {
            proxyResponse = doExecute(servletRequest, proxyRequest);

            int statusCode = proxyResponse.getStatusLine().getStatusCode();
            // servletResponse.setStatus(statusCode,
            // proxyResponse.getStatusLine().getReasonPhrase());
            servletResponse.setStatus(statusCode);

            copyResponseHeaders(proxyResponse, servletRequest, servletResponse);

            if (statusCode == HttpServletResponse.SC_NOT_MODIFIED) {
                servletResponse.setIntHeader(HttpHeaders.CONTENT_LENGTH, 0);
            } else {
                copyResponseEntity(proxyResponse, servletResponse, proxyRequest, servletRequest);
            }

        } catch (Exception e) {
            handleRequestException(proxyRequest, proxyResponse, e);
        } finally {
            if (proxyResponse != null)
                EntityUtils.consumeQuietly(proxyResponse.getEntity());
        }
    }

    /**
     * 使用HttpClient执行代理请求
     * 
     * @param servletRequest
     *            源请求
     * @param proxyRequest
     *            代理请求
     * @return 代理请求的响应结果
     * @throws IOException
     * @author Monk
     * @date 2021年7月15日 下午3:48:02
     */
    protected HttpResponse doExecute(HttpServletRequest servletRequest, HttpRequest proxyRequest)
            throws IOException {
        if (doLog) {
            logger.info("proxy {} uri:{} --> {}", new Object[] { servletRequest.getMethod(),
                    servletRequest.getRequestURI(), proxyRequest.getRequestLine().getUri() });
        }
        return proxyClient.execute(getTargetHost(servletRequest), proxyRequest);
    }

    protected void handleRequestException(HttpRequest proxyRequest, HttpResponse proxyResonse, Exception e)
            throws ServletException, IOException {
        if (proxyRequest instanceof AbortableHttpRequest) {
            AbortableHttpRequest abortableHttpRequest = (AbortableHttpRequest) proxyRequest;
            abortableHttpRequest.abort();
        }
        if (proxyResonse instanceof Closeable) {
            ((Closeable) proxyResonse).close();
        }
        if (e instanceof RuntimeException) {
            throw (RuntimeException) e;
        }
        if (e instanceof ServletException) {
            throw (ServletException) e;
        }
        if (e instanceof IOException) {
            throw (IOException) e;
        }
        throw new RuntimeException(e);
    }

    /**
     * 复制响应头
     * 
     * @param proxyResponse
     *            代理响应
     * @param servletRequest
     *            源请求
     * @param servletResponse
     *            源响应
     * @author Monk
     * @date 2021年7月15日 下午3:02:44
     */
    protected void copyResponseHeaders(HttpResponse proxyResponse, HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) {
        for (Header header : proxyResponse.getAllHeaders()) {
            copyResponseHeader(servletRequest, servletResponse, header);
        }
    }

    /**
     * 复制响应头
     * 
     * @param servletRequest
     *            源请求
     * @param servletResponse
     *            源响应
     * @param header
     *            消息头参数
     * @author Monk
     * @date 2021年7月15日 下午3:03:36
     */
    protected void copyResponseHeader(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
            Header header) {
        String headerName = header.getName();
        if (hopByHopHeaders.containsHeader(headerName))
            return;
        String headerValue = header.getValue();
        if (headerName.equalsIgnoreCase(org.apache.http.cookie.SM.SET_COOKIE)
                || headerName.equalsIgnoreCase(org.apache.http.cookie.SM.SET_COOKIE2)) {
            copyProxyCookie(servletRequest, servletResponse, headerValue);
        } else if (headerName.equalsIgnoreCase(HttpHeaders.LOCATION)) {
            servletResponse.addHeader(headerName, rewriteUrlFromResponse(servletRequest, headerValue));
        } else {
            servletResponse.addHeader(headerName, headerValue);
        }
    }

    /**
     * 复制代理的Cookie
     * 
     * @param servletRequest
     *            源请求
     * @param servletResponse
     *            源响应
     * @param headerValue
     *            Cookie值
     * @author Monk
     * @date 2021年7月15日 下午3:04:40
     */
    protected void copyProxyCookie(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
            String headerValue) {
        for (HttpCookie cookie : HttpCookie.parse(headerValue)) {
            Cookie servletCookie = createProxyCookie(servletRequest, cookie);
            servletResponse.addCookie(servletCookie);
        }
    }

    /**
     * 创建代理Cookie
     * 
     * @param servletRequest
     *            源请求
     * @param cookie
     *            Cookie值
     * @return 代理Cookie
     * @author Monk
     * @date 2021年7月15日 下午3:05:21
     */
    protected Cookie createProxyCookie(HttpServletRequest servletRequest, HttpCookie cookie) {
        String proxyCookieName = getProxyCookieName(cookie);
        Cookie servletCookie = new Cookie(proxyCookieName, cookie.getValue());
        servletCookie.setPath(buildProxyCookiePath(servletRequest));
        servletCookie.setComment(cookie.getComment());
        servletCookie.setMaxAge((int) cookie.getMaxAge());
        servletCookie.setSecure(cookie.getSecure());
        servletCookie.setVersion(cookie.getVersion());
        servletCookie.setHttpOnly(cookie.isHttpOnly());
        return servletCookie;
    }

    /**
     * 获取Cookie名称
     * 
     * @param cookie
     *            cookie对象
     * @return
     * @author Monk
     * @date 2021年7月15日 下午3:05:44
     */
    protected String getProxyCookieName(HttpCookie cookie) {
        return doPreserveCookies ? cookie.getName() : getCookieNamePrefix(cookie.getName()) + cookie.getName();
    }

    protected String buildProxyCookiePath(HttpServletRequest servletRequest) {
        String path = servletRequest.getContextPath();
        path += servletRequest.getServletPath();
        if (path.isEmpty()) {
            path = "/";
        }
        return path;
    }

    /**
     * 复制响应对象体
     * 
     * @param proxyResponse
     *            代理响应对象
     * @param servletResponse
     *            源响应对象
     * @param proxyRequest
     *            代理请求
     * @param servletRequest
     *            源请求
     * @throws IOException
     *             IO异常
     * @author Monk
     * @date 2021年7月15日 下午3:06:04
     */
    protected void copyResponseEntity(HttpResponse proxyResponse, HttpServletResponse servletResponse,
            HttpRequest proxyRequest, HttpServletRequest servletRequest) throws IOException {
        HttpEntity entity = proxyResponse.getEntity();
        if (entity != null) {
            if (entity.isChunked()) {
                InputStream is = entity.getContent();
                OutputStream os = servletResponse.getOutputStream();
                byte[] buffer = new byte[10 * 1024];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                    if (doHandleCompression || is.available() == 0 /* next is.read will block */) {
                        os.flush();
                    }
                }
            } else {
                OutputStream servletOutputStream = servletResponse.getOutputStream();
                entity.writeTo(servletOutputStream);
            }
        }
    }

    private void setXForwardedForHeader(HttpServletRequest servletRequest, HttpRequest proxyRequest) {
        if (doForwardIP) {
            String forHeaderName = "X-Forwarded-For";
            String forHeader = servletRequest.getRemoteAddr();
            String existingForHeader = servletRequest.getHeader(forHeaderName);
            if (existingForHeader != null) {
                forHeader = existingForHeader + ", " + forHeader;
            }
            proxyRequest.setHeader(forHeaderName, forHeader);

            String protoHeaderName = "X-Forwarded-Proto";
            String protoHeader = servletRequest.getScheme();
            proxyRequest.setHeader(protoHeaderName, protoHeader);
        }
    }

    /**
     * 复制源请求的Header信息到代理请求
     * 
     * @param servletRequest
     *            源请求
     * @param proxyRequest
     *            代理请求
     * @author Monk
     * @date 2021年7月15日 下午3:06:47
     */
    protected void copyRequestHeaders(HttpServletRequest servletRequest, HttpRequest proxyRequest) {
        Enumeration<String> enumerationOfHeaderNames = servletRequest.getHeaderNames();
        while (enumerationOfHeaderNames.hasMoreElements()) {
            String headerName = enumerationOfHeaderNames.nextElement();
            copyRequestHeader(servletRequest, proxyRequest, headerName);
        }
    }

    /**
     * 复制源请求的Header信息到代理请求
     * 
     * @param servletRequest
     *            源请求
     * @param proxyRequest
     *            代理请求
     * @param headerName
     *            Header参数名
     * @author Monk
     * @date 2021年7月15日 下午3:07:33
     */
    protected void copyRequestHeader(HttpServletRequest servletRequest, HttpRequest proxyRequest,
            String headerName) {
        if (headerName.equalsIgnoreCase(HttpHeaders.CONTENT_LENGTH))
            return;
        if (hopByHopHeaders.containsHeader(headerName))
            return;
        if (doHandleCompression && headerName.equalsIgnoreCase(HttpHeaders.ACCEPT_ENCODING))
            return;

        Enumeration<String> headers = servletRequest.getHeaders(headerName);
        while (headers.hasMoreElements()) {// sometimes more than one value
            String headerValue = headers.nextElement();
            if (!doPreserveHost && headerName.equalsIgnoreCase(HttpHeaders.HOST)) {
                HttpHost host = getTargetHost(servletRequest);
                headerValue = host.getHostName();
                if (host.getPort() != -1)
                    headerValue += ":" + host.getPort();
            } else if (!doPreserveCookies && headerName.equalsIgnoreCase(org.apache.http.cookie.SM.COOKIE)) {
                headerValue = getRealCookie(headerValue);
            }
            proxyRequest.addHeader(headerName, headerValue);
        }
    }

    /**
     * 获取真实Cookie
     * 
     * @param cookieValue
     *            cookie值
     * @return 真实Cookie
     * @author Monk
     * @date 2021年7月15日 下午3:07:53
     */
    protected String getRealCookie(String cookieValue) {
        StringBuilder escapedCookie = new StringBuilder();
        String cookies[] = cookieValue.split("[;,]");
        for (String cookie : cookies) {
            String cookieSplit[] = cookie.split("=");
            if (cookieSplit.length == 2) {
                String cookieName = cookieSplit[0].trim();
                if (cookieName.startsWith(getCookieNamePrefix(cookieName))) {
                    cookieName = cookieName.substring(getCookieNamePrefix(cookieName).length());
                    if (escapedCookie.length() > 0) {
                        escapedCookie.append("; ");
                    }
                    escapedCookie.append(cookieName).append("=").append(cookieSplit[1].trim());
                }
            }
        }
        return escapedCookie.toString();
    }

    protected String getCookieNamePrefix(String name) {
        return "!Proxy!" + getServletName();
    }

    protected HttpRequest newProxyRequestWithEntity(String method, String proxyRequestUri,
            HttpServletRequest servletRequest) throws IOException {
        HttpEntityEnclosingRequest eProxyRequest = new BasicHttpEntityEnclosingRequest(method, proxyRequestUri);
        eProxyRequest.setEntity(
                new InputStreamEntity(servletRequest.getInputStream(), getContentLength(servletRequest)));
        return eProxyRequest;
    }

    private long getContentLength(HttpServletRequest request) {
        String contentLengthHeader = request.getHeader("Content-Length");
        if (contentLengthHeader != null) {
            return Long.parseLong(contentLengthHeader);
        }
        return -1L;
    }

    /**
     * 对queryString进行Base64编码
     * 
     * @param str
     *            url中的参数
     * @return 编码后的queryString
     * @author Monk
     * @date 2021年7月15日 下午3:37:21
     */
    protected CharSequence encodeUriQuery(CharSequence in, boolean encodePercent) {
        // Note that I can't simply use URI.java to encode because it will
        // escape pre-existing escaped things.
        StringBuilder outBuf = null;
        Formatter formatter = null;
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            boolean escape = true;
            if (c < 128) {
                if (asciiQueryChars.get((int) c) && !(encodePercent && c == '%')) {
                    escape = false;
                }
            } else if (!Character.isISOControl(c) && !Character.isSpaceChar(c)) {// not-ascii
                escape = false;
            }
            if (!escape) {
                if (outBuf != null)
                    outBuf.append(c);
            } else {
                // escape
                if (outBuf == null) {
                    outBuf = new StringBuilder(in.length() + 5 * 3);
                    outBuf.append(in, 0, i);
                    formatter = new Formatter(outBuf);
                }
                // leading %, 0 padded, width 2, capital hex
                formatter.format("%%%02X", (int) c);// TODO
            }
        }
        return outBuf != null ? outBuf : in;
    }

    protected static final BitSet asciiQueryChars;
    static {
        char[] c_unreserved = "_-!.~'()*".toCharArray();// plus alphanum
        char[] c_punct = ",;:$&+=".toCharArray();
        char[] c_reserved = "/[]@".toCharArray();// plus punct. Exclude '?';
                                                 // RFC-2616 3.2.2

        asciiQueryChars = new BitSet(128);
        for (char c = 'a'; c <= 'z'; c++)
            asciiQueryChars.set((int) c);
        for (char c = 'A'; c <= 'Z'; c++)
            asciiQueryChars.set((int) c);
        for (char c = '0'; c <= '9'; c++)
            asciiQueryChars.set((int) c);
        for (char c : c_unreserved)
            asciiQueryChars.set((int) c);
        for (char c : c_punct)
            asciiQueryChars.set((int) c);
        for (char c : c_reserved)
            asciiQueryChars.set((int) c);

        asciiQueryChars.set((int) '%');// leave existing percent escapes in
                                       // place
    }

    /**
     * 重写请求url
     * 
     * @param servletRequest
     *            源请求
     * @return 重写后的请求url
     * @author Monk
     * @date 2021年7月15日 下午3:37:55
     */
    protected String rewriteUrlFromRequest(HttpServletRequest servletRequest) {
        StringBuilder uri = new StringBuilder(500);
        uri.append(getTargetUri(servletRequest));

        String sourceUri = servletRequest.getRequestURI();
        String contentPath = servletRequest.getContextPath();
        uri.append(sourceUri.replace(contentPath + "/proxy", ""));

        String pathInfo = rewritePathInfoFromRequest(servletRequest);
        if (pathInfo != null) {
            uri.append(encodeUriQuery(pathInfo, true));
        }
        String queryString = servletRequest.getQueryString();
        String fragment = null;
        if (queryString != null) {
            int fragIdx = queryString.indexOf('#');
            if (fragIdx >= 0) {
                fragment = queryString.substring(fragIdx + 1);
                queryString = queryString.substring(0, fragIdx);
            }
        }

        queryString = rewriteQueryStringFromRequest(servletRequest, queryString);
        if (queryString != null && queryString.length() > 0) {
            uri.append('?');
            uri.append(encodeUriQuery(queryString, false));
        }

        if (doSendUrlFragment && fragment != null) {
            uri.append('#');
            uri.append(encodeUriQuery(fragment, false));
        }
        return uri.toString();
    }

    protected String getTargetUri(HttpServletRequest servletRequest) {
        return (String) servletRequest.getAttribute(ATTR_TARGET_URI);
    }

    protected HttpHost getTargetHost(HttpServletRequest servletRequest) {
        return (HttpHost) servletRequest.getAttribute(ATTR_TARGET_HOST);
    }

    protected String rewriteQueryStringFromRequest(HttpServletRequest servletRequest, String queryString) {
        return queryString;
    }

    protected String rewritePathInfoFromRequest(HttpServletRequest servletRequest) {
        return servletRequest.getPathInfo();
    }

    /**
     * 重写响应头中的location地址
     * 
     * @param servletRequest
     *            源请求
     * @param theUrl
     *            响应中的localtion地址
     * @return 重写后的location地址
     * @author Monk
     * @date 2021年7月15日 下午3:45:23
     */
    protected String rewriteUrlFromResponse(HttpServletRequest servletRequest, String theUrl) {
        final String targetUri = getTargetUri(servletRequest);
        if (theUrl.startsWith(targetUri)) {
            StringBuffer curUrl = servletRequest.getRequestURL();// no query
            int pos;
            if ((pos = curUrl.indexOf("://")) >= 0) {
                if ((pos = curUrl.indexOf("/", pos + 3)) >= 0) {
                    curUrl.setLength(pos);
                }
            }
            curUrl.append(servletRequest.getContextPath());
            curUrl.append(servletRequest.getServletPath());
            curUrl.append(theUrl, targetUri.length(), theUrl.length());
            return curUrl.toString();
        }
        return theUrl;
    }

    public String getTargetUri() {
        return targetUri;
    }

    public void setTargetUri(String targetUri) {
        this.targetUri = targetUri;
    }

    public URI getTargetUriObj() {
        return targetUriObj;
    }

    public void setTargetUriObj(URI targetUriObj) {
        this.targetUriObj = targetUriObj;
    }

    public HttpHost getTargetHost() {
        return targetHost;
    }

    public void setTargetHost(HttpHost targetHost) {
        this.targetHost = targetHost;
    }

    public boolean isDoHandleCompression() {
        return doHandleCompression;
    }

    public void setDoHandleCompression(boolean doHandleCompression) {
        this.doHandleCompression = doHandleCompression;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public boolean isUseSystemProperties() {
        return useSystemProperties;
    }

    public void setUseSystemProperties(boolean useSystemProperties) {
        this.useSystemProperties = useSystemProperties;
    }

    public boolean isDoSendUrlFragment() {
        return doSendUrlFragment;
    }

    public void setDoSendUrlFragment(boolean doSendUrlFragment) {
        this.doSendUrlFragment = doSendUrlFragment;
    }

    public boolean isDoPreserveHost() {
        return doPreserveHost;
    }

    public void setDoPreserveHost(boolean doPreserveHost) {
        this.doPreserveHost = doPreserveHost;
    }

    public boolean isDoPreserveCookies() {
        return doPreserveCookies;
    }

    public void setDoPreserveCookies(boolean doPreserveCookies) {
        this.doPreserveCookies = doPreserveCookies;
    }

    public boolean isDoHandleRedirects() {
        return doHandleRedirects;
    }

    public void setDoHandleRedirects(boolean doHandleRedirects) {
        this.doHandleRedirects = doHandleRedirects;
    }

    public String getServletName() {
        return servletName;
    }

    public void setServletName(String servletName) {
        this.servletName = servletName;
    }

}
