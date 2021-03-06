package com.monk.service;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.net.URI;
import java.util.BitSet;
import java.util.Enumeration;
import java.util.Formatter;

import javax.annotation.PostConstruct;
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
import org.apache.http.client.methods.AbortableHttpRequest;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.HeaderGroup;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monk.properties.HttpClientProperties;
import com.monk.properties.ProxyProperties;
import com.monk.util.HttpClientFactory;
import com.monk.util.ProxyUtils;

@Service
public class ProxyServiceImpl implements ProxyService {

    private static final Logger logger = LoggerFactory.getLogger(ProxyUtils.class);

    private URI targetUriObj;
    private HttpHost targetHost;
    private static final HeaderGroup hopByHopHeaders = new HeaderGroup();

    private String proxyType;
    private String targetUri;

    private boolean doLog = true;
    private boolean doForwardIP = true;
    private boolean doSendUrlFragment = true;
    private boolean doHandleCompression = false;
    private boolean doPreserveHost = false;
    private boolean doPreserveCookies = false;
    private static final String ATTR_TARGET_URI = "targetUri";
    private static final String ATTR_TARGET_HOST = "targetHost";

    @Autowired
    private ProxyProperties proxyProperties;

    @PostConstruct
    public void init() {
        // ???????????????????????????
        this.doLog = proxyProperties.isDoLog();
        this.doForwardIP = proxyProperties.isDoForwardIP();
        this.doSendUrlFragment = proxyProperties.isDoSendUrlFragment();
        this.doHandleCompression = proxyProperties.isDoHandleCompression();
        this.doPreserveHost = proxyProperties.isDoPreserveHost();
        this.doPreserveCookies = proxyProperties.isDoPreserveCookies();

        initHeader();
    }

    /**
     * ????????????
     * 
     * @param targetUri
     * @param proxyType
     * @throws ServletException
     * @author Monk
     * @date 2021???8???13??? ??????7:23:12
     */
    ProxyServiceImpl(String targetUri, String proxyType) throws ServletException {
        this.proxyType = proxyType;
        this.targetUri = targetUri;
        initTargetConfig();
    }

    /**
     * ?????????Header??????
     * 
     * @author Monk
     * @date 2021???7???15??? ??????11:47:53
     */
    private void initHeader() {
        String[] headers = new String[] { "Connection", "Keep-Alive", "Proxy-Authenticate", "Proxy-Authorization",
                "TE", "Trailers", "Transfer-Encoding", "Upgrade" };
        for (String header : headers) {
            hopByHopHeaders.addHeader(new BasicHeader(header, null));
        }
    }

    /**
     * ???????????????????????????
     * 
     * @throws ServletException
     * @author Monk
     * @date 2021???7???15??? ??????2:46:19
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

    @Override
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
     * ?????????????????????
     * 
     * @param proxyResponse
     *            ??????????????????
     * @param servletResponse
     *            ???????????????
     * @param proxyRequest
     *            ????????????
     * @param servletRequest
     *            ?????????
     * @throws IOException
     *             IO??????
     * @author Monk
     * @date 2021???7???15??? ??????3:06:04
     */
    private void copyResponseEntity(HttpResponse proxyResponse, HttpServletResponse servletResponse,
            HttpRequest proxyRequest, HttpServletRequest servletRequest) throws IOException {
        HttpEntity entity = proxyResponse.getEntity();
        if (entity != null) {
            if (entity.isChunked()) {
                InputStream input = null;
                OutputStream out = null;
                try {
                    input = entity.getContent();
                    out = servletResponse.getOutputStream();
                    byte[] buffer = new byte[10 * 1024];
                    int read;
                    while ((read = input.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                        if (doHandleCompression || input.available() == 0) {
                            out.flush();
                        }
                    }
                } finally {
                    if (null != out) {
                        out.close();
                    }
                    if (null != input) {
                        input.close();
                    }
                }
            } else {
                OutputStream servletOutputStream = servletResponse.getOutputStream();
                entity.writeTo(servletOutputStream);
            }
        }
    }

    /**
     * ??????HttpClient??????????????????
     * 
     * @param servletRequest
     *            ?????????
     * @param proxyRequest
     *            ????????????
     * @return ???????????????????????????
     * @throws IOException
     * @author Monk
     * @date 2021???7???15??? ??????3:48:02
     */
    private HttpResponse doExecute(HttpServletRequest servletRequest, HttpRequest proxyRequest)
            throws IOException {
        if (doLog) {
            logger.info("proxy {} uri:{} --> {}", new Object[] { servletRequest.getMethod(),
                    servletRequest.getRequestURI(), proxyRequest.getRequestLine().getUri() });
        }
        return HttpClientFactory.getHttpClient().execute(getTargetHost(servletRequest), proxyRequest);
    }

    /**
     * ?????????????????????Host??????
     * 
     * @param servletRequest
     *            ????????????
     * @return ???????????????Host??????
     * @author Monk
     * @date 2021???7???21??? ??????10:22:19
     */
    private HttpHost getTargetHost(HttpServletRequest servletRequest) {
        return (HttpHost) servletRequest.getAttribute(ATTR_TARGET_HOST);
    }

    /**
     * ????????????????????????queryString??????(???????????????"?"???????????????)
     * 
     * @param servletRequest
     *            ????????????
     * @param queryString
     *            ????????????
     * @return ????????????
     * @author Monk
     * @date 2021???7???21??? ??????10:22:19
     */
    private String rewriteQueryStringFromRequest(HttpServletRequest servletRequest, String queryString) {
        return queryString;
    }

    /**
     * ??????????????????
     * 
     * @param proxyRequest
     *            ????????????
     * @param proxyResonse
     *            ????????????
     * @param e
     *            ??????
     * @throws ServletException
     *             Servlet??????
     * @throws IOException
     *             IO??????
     * @author Monk
     * @date 2021???7???16??? ??????10:24:53
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
     * ???????????????
     * 
     * @param proxyResponse
     *            ????????????
     * @param servletRequest
     *            ?????????
     * @param servletResponse
     *            ?????????
     * @author Monk
     * @date 2021???7???15??? ??????3:02:44
     */
    private void copyResponseHeaders(HttpResponse proxyResponse, HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) {
        for (Header header : proxyResponse.getAllHeaders()) {
            copyResponseHeader(servletRequest, servletResponse, header);
        }
    }

    /**
     * ???????????????
     * 
     * @param servletRequest
     *            ?????????
     * @param servletResponse
     *            ?????????
     * @param header
     *            ???????????????
     * @author Monk
     * @date 2021???7???15??? ??????3:03:36
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
     * ?????????????????????location??????
     * 
     * @param servletRequest
     *            ?????????
     * @param theUrl
     *            ????????????localtion??????
     * @return ????????????location??????
     * @author Monk
     * @date 2021???7???15??? ??????3:45:23
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

    /**
     * ???????????????Cookie
     * 
     * @param servletRequest
     *            ?????????
     * @param servletResponse
     *            ?????????
     * @param headerValue
     *            Cookie???
     * @author Monk
     * @date 2021???7???15??? ??????3:04:40
     */
    private void copyProxyCookie(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
            String headerValue) {
        for (HttpCookie cookie : HttpCookie.parse(headerValue)) {
            Cookie servletCookie = createProxyCookie(servletRequest, cookie);
            servletResponse.addCookie(servletCookie);
        }
    }

    /**
     * ????????????Cookie
     * 
     * @param servletRequest
     *            ?????????
     * @param cookie
     *            Cookie???
     * @return ??????Cookie
     * @author Monk
     * @date 2021???7???15??? ??????3:05:21
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
     * ????????????Cookie??????
     * 
     * @param cookie
     *            cookie??????
     * @return ??????Cookie??????
     * @author Monk
     * @date 2021???7???15??? ??????3:05:44
     */
    private String getProxyCookieName(HttpCookie cookie) {
        return doPreserveCookies ? cookie.getName() : getCookieNamePrefix() + cookie.getName();
    }

    /**
     * ??????Cookie???Path
     * 
     * @param servletRequest
     *            ????????????
     * @return Cookie???Path
     * @author Monk
     * @date 2021???7???21??? ??????10:13:58
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
     * ??????X-Forwarded-For???????????????????????????
     * 
     * @param servletRequest
     *            ???????????????
     * @param proxyRequest
     *            ??????????????????
     * @author Monk
     * @date 2021???7???21??? ??????10:16:17
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
     * ??????????????????Header?????????????????????
     * 
     * @param servletRequest
     *            ?????????
     * @param proxyRequest
     *            ????????????
     * @author Monk
     * @date 2021???7???15??? ??????3:06:47
     */
    private void copyRequestHeaders(HttpServletRequest servletRequest, HttpRequest proxyRequest) {
        Enumeration<String> enumerationOfHeaderNames = servletRequest.getHeaderNames();
        while (enumerationOfHeaderNames.hasMoreElements()) {
            String headerName = enumerationOfHeaderNames.nextElement();
            copyRequestHeader(servletRequest, proxyRequest, headerName);
        }

        // ???????????????Header??????
        String url = servletRequest.getRequestURL().toString();
        String path = servletRequest.getContextPath();
        String sourcePath = url.substring(0, url.indexOf(path) + path.length()) + "/proxy/" + proxyType;
        proxyRequest.addHeader("isProxy", "true");
        proxyRequest.addHeader("sourcePath", sourcePath);
    }

    /**
     * ??????????????????Header?????????????????????
     * 
     * @param servletRequest
     *            ?????????
     * @param proxyRequest
     *            ????????????
     * @param headerName
     *            Header?????????
     * @author Monk
     * @date 2021???7???15??? ??????3:07:33
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
     * ????????????Cookie
     * 
     * @param cookieValue
     *            cookie???
     * @return ??????Cookie
     * @author Monk
     * @date 2021???7???15??? ??????3:07:53
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
     * ????????????Cookie??????????????????????????????
     * 
     * @return ??????Cookie????????????
     * @author Monk
     * @date 2021???7???21??? ??????10:17:44
     */
    private String getCookieNamePrefix() {
        return "!Proxy!" + proxyType;
    }

    /**
     * ????????????????????????
     * 
     * @param method
     *            ????????????
     * @param proxyRequestUri
     *            ????????????uri
     * @param servletRequest
     *            ???????????????
     * @return ??????????????????
     * @author Monk
     * @date 2021???7???21??? ??????10:17:44
     */
    private HttpRequest newProxyRequestWithEntity(String method, String proxyRequestUri,
            HttpServletRequest servletRequest) throws IOException {
        HttpEntityEnclosingRequest eProxyRequest = new BasicHttpEntityEnclosingRequest(method, proxyRequestUri);
        eProxyRequest.setEntity(
                new InputStreamEntity(servletRequest.getInputStream(), getContentLength(servletRequest)));
        return eProxyRequest;
    }

    /**
     * ???????????????????????????Content-Length??????
     * 
     * @param request
     *            ???????????????
     * @return ?????????????????????Content-Length??????
     * @author Monk
     * @date 2021???7???21??? ??????10:17:44
     */
    private long getContentLength(HttpServletRequest request) {
        String contentLengthHeader = request.getHeader("Content-Length");
        if (contentLengthHeader != null) {
            return Long.parseLong(contentLengthHeader);
        }
        return -1L;
    }

    /**
     * ???queryString??????Base64??????
     * 
     * @param str
     *            url????????????
     * @param encodePercent
     *            ??????%
     * @return ????????????queryString
     * @author Monk
     * @date 2021???7???15??? ??????3:37:21
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
     * ????????????url
     * 
     * @param servletRequest
     *            ?????????
     * @return ??????????????????url
     * @author Monk
     * @date 2021???7???15??? ??????3:37:55
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
     * ????????????????????????pathInfo
     * 
     * @param servletRequest
     *            ????????????
     * @return ??????????????????pathInfo
     * @author Monk
     * @date 2021???7???21??? ??????10:22:19
     */
    private String rewritePathInfoFromRequest(HttpServletRequest servletRequest) {
        return servletRequest.getPathInfo();
    }

    /**
     * ??????????????????
     * 
     * @param servletRequest
     *            ????????????
     * @return ????????????
     * @author Monk
     * @date 2021???7???21??? ??????10:22:19
     */
    private String getTargetUri(HttpServletRequest servletRequest) {
        return (String) servletRequest.getAttribute(ATTR_TARGET_URI);
    }

}
