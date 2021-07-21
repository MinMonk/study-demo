package com.monk.controller;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.net.URI;
import java.util.BitSet;
import java.util.Enumeration;
import java.util.Formatter;

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
 * 反向代理服务类
 * 
 * @author Monk
 * @version V1.0
 * @date 2021年7月15日 上午11:08:51
 */
@SuppressWarnings("deprecation")
public class ProxyService {

    private static final Logger logger = LoggerFactory.getLogger(ProxyService.class);

    private URI targetUriObj;
    private HttpHost targetHost;
    private HttpClient proxyClient;
    private static final HeaderGroup hopByHopHeaders = new HeaderGroup();

    private int connectTimeout = -1;
    private int readTimeout = -1;
    private int connectionRequestTimeout = -1;
    private int maxConnections = -1;
    private String proxyType;
    private String targetUri;

    private boolean doLog = true;
    private boolean doForwardIP = true;
    private boolean useSystemProperties = true;
    private boolean doSendUrlFragment = true;
    private boolean doHandleCompression = false;
    private boolean doPreserveHost = false;
    private boolean doPreserveCookies = false;
    private boolean doHandleRedirects = false;
    private static final String ATTR_TARGET_URI = "targetUri";
    private static final String ATTR_TARGET_HOST = "targetHost";

    /**
     * 私有化构造方法
     * 
     * @param targetUri
     *            目标端地址
     * @param proxyType
     *            代理类型
     * @throws ServletException
     * @author Monk
     * @date 2021年7月15日 下午4:52:42
     */
    public ProxyService(String targetUri, String proxyType) throws ServletException {
        this.proxyType = proxyType;
        this.targetUri = targetUri;
        initTargetConfig();
        proxyClient = createHttpClient();
        initHeader();
    }

    /**
     * 获取代理服务类
     * 
     * @param proxyType
     *            代理类型
     * @return 代理服务类
     * @throws ServletException
     * @author Monk
     * @date 2021年7月15日 下午4:53:04
     */
    public static ProxyService getProxyService(String targetUriValue, String proxyType) throws ServletException {
        return new ProxyService(targetUriValue, proxyType);
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
            servletResponse.setStatus(statusCode, proxyResponse.getStatusLine().getReasonPhrase());

            copyResponseHeaders(proxyResponse, servletRequest, servletResponse);

            if (statusCode == HttpServletResponse.SC_NOT_MODIFIED) {
                servletResponse.setIntHeader(HttpHeaders.CONTENT_LENGTH, 0);
            } else {
                copyResponseEntity(proxyResponse, servletResponse, proxyRequest, servletRequest);
            }

        } catch (Exception e) {
            handleRequestException(proxyRequest, proxyResponse, e);
        } finally {
            if (proxyResponse != null) {
                EntityUtils.consumeQuietly(proxyResponse.getEntity());
            }
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
    private HttpResponse doExecute(HttpServletRequest servletRequest, HttpRequest proxyRequest)
            throws IOException {
        if (doLog) {
            logger.info("proxy {} uri:{} --> {}", new Object[] { servletRequest.getMethod(),
                    servletRequest.getRequestURI(), proxyRequest.getRequestLine().getUri() });
        }
        return proxyClient.execute(getTargetHost(servletRequest), proxyRequest);
    }

    /**
     * 处理请求异常
     * 
     * @param proxyRequest
     *            代理请求
     * @param proxyResonse
     *            代理响应
     * @param e
     *            异常
     * @throws ServletException
     *             Servlet异常
     * @throws IOException
     *             IO异常
     * @author Monk
     * @date 2021年7月16日 上午10:24:53
     */
    private void handleRequestException(HttpRequest proxyRequest, HttpResponse proxyResonse, Exception e)
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
    private void copyResponseHeaders(HttpResponse proxyResponse, HttpServletRequest servletRequest,
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
    private void copyResponseHeader(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
            Header header) {
        String headerName = header.getName();
        if (hopByHopHeaders.containsHeader(headerName)) {
            return;
        }
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
    private void copyProxyCookie(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
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
    private Cookie createProxyCookie(HttpServletRequest servletRequest, HttpCookie cookie) {
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
     * 获取代理Cookie名称
     * 
     * @param cookie
     *            cookie对象
     * @return 代理Cookie名称
     * @author Monk
     * @date 2021年7月15日 下午3:05:44
     */
    private String getProxyCookieName(HttpCookie cookie) {
        return doPreserveCookies ? cookie.getName() : getCookieNamePrefix() + cookie.getName();
    }

    /**
     * 构建Cookie的Path
     * 
     * @param servletRequest
     *            请求对象
     * @return Cookie的Path
     * @author Monk
     * @date 2021年7月21日 上午10:13:58
     */
    private String buildProxyCookiePath(HttpServletRequest servletRequest) {
        String path = servletRequest.getContextPath();
        path = path + "/proxy/" + proxyType;
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
    private void copyResponseEntity(HttpResponse proxyResponse, HttpServletResponse servletResponse,
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
                    if (doHandleCompression || is.available() == 0) {
                        os.flush();
                    }
                }
            } else {
                OutputStream servletOutputStream = servletResponse.getOutputStream();
                entity.writeTo(servletOutputStream);
            }
        }
    }

    /**
     * 填充X-Forwarded-For属性到代理请求头中
     * 
     * @param servletRequest
     *            源请求对象
     * @param proxyRequest
     *            代理请求对象
     * @author Monk
     * @date 2021年7月21日 上午10:16:17
     */
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
    private void copyRequestHeaders(HttpServletRequest servletRequest, HttpRequest proxyRequest) {
        Enumeration<String> enumerationOfHeaderNames = servletRequest.getHeaderNames();
        while (enumerationOfHeaderNames.hasMoreElements()) {
            String headerName = enumerationOfHeaderNames.nextElement();
            copyRequestHeader(servletRequest, proxyRequest, headerName);
        }

        // 添加自定义Header属性
        String url = servletRequest.getRequestURL().toString();
        String path = servletRequest.getContextPath();
        String sourcePath = url.substring(0, url.indexOf(path) + path.length()) + "/proxy/" + proxyType;
        proxyRequest.addHeader("isProxy", "true");
        proxyRequest.addHeader("sourcePath", sourcePath);
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
    private void copyRequestHeader(HttpServletRequest servletRequest, HttpRequest proxyRequest,
            String headerName) {
        if (headerName.equalsIgnoreCase(HttpHeaders.CONTENT_LENGTH)) {
            return;
        }

        if (hopByHopHeaders.containsHeader(headerName)) {
            return;
        }
        if (doHandleCompression && headerName.equalsIgnoreCase(HttpHeaders.ACCEPT_ENCODING)) {
            return;
        }

        Enumeration<String> headers = servletRequest.getHeaders(headerName);
        while (headers.hasMoreElements()) {
            String headerValue = headers.nextElement();
            if (!doPreserveHost && headerName.equalsIgnoreCase(HttpHeaders.HOST)) {
                HttpHost host = getTargetHost(servletRequest);
                headerValue = host.getHostName();
                if (host.getPort() != -1) {
                    headerValue += ":" + host.getPort();
                }
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
    private String getRealCookie(String cookieValue) {
        StringBuilder escapedCookie = new StringBuilder();
        String cookies[] = cookieValue.split("[;,]");
        for (String cookie : cookies) {
            String cookieSplit[] = cookie.split("=");
            if (cookieSplit.length == 2) {
                String cookieName = cookieSplit[0].trim();
                if (cookieName.startsWith(getCookieNamePrefix())) {
                    cookieName = cookieName.substring(getCookieNamePrefix().length());
                    if (escapedCookie.length() > 0) {
                        escapedCookie.append("; ");
                    }
                    escapedCookie.append(cookieName).append("=").append(cookieSplit[1].trim());
                }
            }
        }
        return escapedCookie.toString();
    }

    /**
     * 获取代理Cookie名称的前缀，用于区分
     * 
     * @return 代理Cookie名称前缀
     * @author Monk
     * @date 2021年7月21日 上午10:17:44
     */
    private String getCookieNamePrefix() {
        return "!Proxy!" + proxyType;
    }

    /**
     * 创建代理请求对象
     * 
     * @param method
     *            请求类型
     * @param proxyRequestUri
     *            代理请求uri
     * @param servletRequest
     *            源请求对象
     * @return 代理请求对象
     * @author Monk
     * @date 2021年7月21日 上午10:17:44
     */
    private HttpRequest newProxyRequestWithEntity(String method, String proxyRequestUri,
            HttpServletRequest servletRequest) throws IOException {
        HttpEntityEnclosingRequest eProxyRequest = new BasicHttpEntityEnclosingRequest(method, proxyRequestUri);
        eProxyRequest.setEntity(
                new InputStreamEntity(servletRequest.getInputStream(), getContentLength(servletRequest)));
        return eProxyRequest;
    }

    /**
     * 获取请求对象头中的Content-Length属性
     * 
     * @param request
     *            源请求对象
     * @return 请求对象头中的Content-Length属性
     * @author Monk
     * @date 2021年7月21日 上午10:17:44
     */
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
     * @param encodePercent
     *            编码%
     * @return 编码后的queryString
     * @author Monk
     * @date 2021年7月15日 下午3:37:21
     */
    private CharSequence encodeUriQuery(CharSequence in, boolean encodePercent) {
        StringBuilder outBuf = null;
        Formatter formatter = null;
        for (int i = 0; i < in.length(); i++) {
            char ch = in.charAt(i);
            boolean escape = true;
            if (ch < 128) {
                if (asciiQueryChars.get((int) ch) && !(encodePercent && ch == '%')) {
                    escape = false;
                }
            } else if (!Character.isISOControl(ch) && !Character.isSpaceChar(ch)) {
                escape = false;
            }
            if (!escape) {
                if (outBuf != null) {
                    outBuf.append(ch);
                }
            } else {
                if (outBuf == null) {
                    outBuf = new StringBuilder(in.length() + 5 * 3);
                    outBuf.append(in, 0, i);
                    formatter = new Formatter(outBuf);
                }
                formatter.format("%%%02X", (int) ch);
            }
        }
        return outBuf != null ? outBuf : in;
    }

    private static final BitSet asciiQueryChars;
    static {
        char[] c_unreserved = "_-!.~'()*".toCharArray();
        char[] c_punct = ",;:$&+=".toCharArray();
        char[] c_reserved = "/[]@".toCharArray();

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

        asciiQueryChars.set((int) '%');
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
    private String rewriteUrlFromRequest(HttpServletRequest servletRequest) {
        StringBuilder uri = new StringBuilder(500);
        uri.append(getTargetUri(servletRequest));

        String sourceUri = servletRequest.getRequestURI();
        String contentPath = servletRequest.getContextPath();
        uri.append(sourceUri.replace(contentPath + "/proxy/" + proxyType, ""));

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

    /**
     * 获取代理地址
     * 
     * @param servletRequest
     *            请求对象
     * @return 代理地址
     * @author Monk
     * @date 2021年7月21日 上午10:22:19
     */
    private String getTargetUri(HttpServletRequest servletRequest) {
        return (String) servletRequest.getAttribute(ATTR_TARGET_URI);
    }

    /**
     * 获取代理地址的Host信息
     * 
     * @param servletRequest
     *            请求对象
     * @return 代理地址的Host信息
     * @author Monk
     * @date 2021年7月21日 上午10:22:19
     */
    private HttpHost getTargetHost(HttpServletRequest servletRequest) {
        return (HttpHost) servletRequest.getAttribute(ATTR_TARGET_HOST);
    }

    /**
     * 重写请求对象中的queryString参数(请求地址中"?"后跟的参数)
     * 
     * @param servletRequest
     *            请求对象
     * @param queryString
     *            查询参数
     * @return 代理地址
     * @author Monk
     * @date 2021年7月21日 上午10:22:19
     */
    private String rewriteQueryStringFromRequest(HttpServletRequest servletRequest, String queryString) {
        return queryString;
    }

    /**
     * 重写请求对象中的pathInfo
     * 
     * @param servletRequest
     *            请求对象
     * @return 请求对象中的pathInfo
     * @author Monk
     * @date 2021年7月21日 上午10:22:19
     */
    private String rewritePathInfoFromRequest(HttpServletRequest servletRequest) {
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
    private String rewriteUrlFromResponse(HttpServletRequest servletRequest, String theUrl) {
        final String targetUri = getTargetUri(servletRequest);
        if (theUrl.startsWith(targetUri)) {
            StringBuffer curUrl = servletRequest.getRequestURL();
            int pos;
            if ((pos = curUrl.indexOf("://")) >= 0) {
                if ((pos = curUrl.indexOf("/", pos + 3)) >= 0) {
                    curUrl.setLength(pos);
                }
            }
            curUrl.append(servletRequest.getContextPath());
            curUrl.append(servletRequest.getServletPath());
            return curUrl.toString();
        }
        return theUrl;
    }
}
