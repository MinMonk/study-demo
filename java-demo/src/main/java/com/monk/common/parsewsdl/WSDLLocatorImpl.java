package com.monk.common.parsewsdl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

import javax.wsdl.xml.WSDLLocator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import com.ibm.wsdl.util.StringUtils;

/**
 * WSDL资源加载
 * 
 * @author houyongchuan
 *
 */
public class WSDLLocatorImpl implements WSDLLocator {
    private static final Logger logger = LoggerFactory.getLogger(WSDLLocatorImpl.class);
    private String url;
    private String last;
    private String userName;
    private String passwd;
    private int httpResponseCode;
    private String httpResponseMessage;
    private int maxContentLength = 1024 * 1024 * 8;

    public WSDLLocatorImpl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    @Override
    public InputSource getBaseInputSource() {
        URL contextURL = null;
        try {
            contextURL = (url != null) ? StringUtils.getURL(null, url) : null;
            logger.info("Retrieving document at '" + url + "'"
                    + (contextURL == null ? "." : ", relative to '" + contextURL + "'."));
            last = url.toString();
            // InputStream inputStream = getContentAsInputStream(contextURL);
            // update 2019-11-25 改成用httpclient方式获取url数据
            InputStream inputStream = HttpCilentUtil.getGetDateByUrl(url, userName, passwd);
            InputSource inputSource = new InputSource(inputStream);
            return inputSource;
        } catch (Exception e) {
            throw new RuntimeException("Unable to resolve imported document at '" + last
                    + (contextURL == null ? "'." : "', relative to '" + contextURL + "'."), e);
        }
    }

    @Override
    public InputSource getImportInputSource(String contextURI, String locationURI) {
        try {
            URL contextURL = (contextURI != null) ? StringUtils.getURL(null, contextURI) : null;
            URL url = null;
            url = StringUtils.getURL(contextURL, locationURI);
            last = url.toString();
            logger.info("Retrieving document at '" + url + "'"
                    + (contextURI == null ? "." : ", relative to '" + contextURI + "'."));

            // InputStream inputStream = getContentAsInputStream(url);
            // update 2019-11-25 改成用httpclient方式获取url数据
            InputStream inputStream = HttpCilentUtil.getGetDateByUrl(url.toString(), userName, passwd);
            InputSource inputSource = new InputSource(inputStream);
            return inputSource;
        } catch (Exception e) {
            throw new RuntimeException("Unable to resolve imported document at '" + last
                    + (contextURI == null ? "'." : "', relative to '" + contextURI + "'."), e);
        }
    }

    /**
     * 获取wsdl url 资源输入流
     * 
     * @param url
     * @return
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IOException
     */
    public InputStream getContentAsInputStream(URL url) throws SecurityException, IllegalArgumentException,
            IOException {
        if (url == null) {
            throw new IllegalArgumentException("URL cannot be null.");
        }

        try {
            String author = null;
            // 如果有设置用户密码，计算认证信息
            if (userName != null && passwd != null) {
                author = "Basic " + Base64.getEncoder().encodeToString((userName + ":" + passwd).getBytes());
            }

            URLConnection conn = url.openConnection();
            // 如果有认证信息，则添加httpbasic认证信息
            if (author != null) {
                logger.info("add http basic authorization info in URL RequestProperty,URL[{}]", url);
                conn.setRequestProperty("Authorization", author);
            }
            // 设置超时
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(30000);
            ByteArrayOutputStream baos = null;
            InputStream inputStream = null;
            byte[] content = null;
            HttpURLConnection httpConn = null;
            HttpURLConnection httpRedirectConn = null;
            if (conn instanceof HttpURLConnection) {
                httpConn = (HttpURLConnection) conn;
            }

            // 获取输入流
            try {
                if (httpConn != null && httpConn.getResponseCode() == 200) {
                    inputStream = conn.getInputStream();
                } else if (httpConn != null
                        && (httpConn.getResponseCode() == 301 || httpConn.getResponseCode() == 302)) {
                    // 重定向，单次重定向，多次不考虑
                    // 得到重定向的地址
                    String location = httpConn.getHeaderField("Location");
                    logger.info("Request URL[{}] got response: {}", url, conn.getHeaderField(0));
                    logger.info("Redirect url: {} ", location);
                    URL u1 = new URL(location);
                    httpRedirectConn = (HttpURLConnection) u1.openConnection();
                    // 如果有认证信息，则添加httpbasic认证信息
                    if (author != null) {
                        logger.info("add http basic authorization info in URL RequestProperty,URL[{}]", url);
                        httpRedirectConn.setRequestProperty("Authorization", author);
                    }
                    httpRedirectConn.setConnectTimeout(10000);
                    httpRedirectConn.setReadTimeout(30000);
                    inputStream = httpRedirectConn.getInputStream();
                } else if (httpConn != null) {
                    throw new IOException("Request URL[" + url + "] got response: " + conn.getHeaderField(0));
                } else {
                    inputStream = conn.getInputStream();
                }

                // 读取内容到缓存中
                int readedContentLength = 0;
                baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[2048];
                int len;
                while ((len = inputStream.read(buffer)) > -1) {
                    baos.write(buffer, 0, len);
                    readedContentLength += len;
                    if (readedContentLength > maxContentLength) {
                        throw new IOException(" Over Max Content Length limit:" + maxContentLength);
                    }
                }
                baos.flush();
                content = baos.toByteArray();
                // 关闭流
                inputStream.close();
                baos.close();
                baos = null;
                inputStream = null;
            } catch (IOException e) {
                if (conn instanceof HttpURLConnection) {
                    httpConn = (HttpURLConnection) conn;
                    httpResponseCode = httpConn.getResponseCode();
                    httpResponseMessage = httpConn.getResponseMessage();
                    // 关闭http连接
                    httpConn.disconnect();
                    logger.error("Request URL[" + url + "] got response: " + conn.getHeaderField(0));
                }
                throw e;
            } finally {
                if (conn != null && conn instanceof HttpURLConnection) {
                    httpConn = (HttpURLConnection) conn;
                    httpResponseCode = httpConn.getResponseCode();
                    httpResponseMessage = httpConn.getResponseMessage();
                    // 关闭http连接
                    httpConn.disconnect();
                    logger.error("Request URL[" + url + "] got response: " + conn.getHeaderField(0));
                }

                if (httpRedirectConn != null) {
                    httpRedirectConn.disconnect();
                }

                if (inputStream != null) {
                    inputStream.close();
                }
            }

            if (content == null) {
                throw new IllegalArgumentException("No content.");
            }
            if (logger.isDebugEnabled()) {
                logger.debug(new String(content));
            }
            return new ByteArrayInputStream(content);
        } catch (SecurityException e) {
            throw new SecurityException("Your JVM's SecurityManager has " + "disallowed this.");
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("This file was not found: " + url);
        }
    }

    @Override
    public String getBaseURI() {
        return url;
    }

    @Override
    public String getLatestImportURI() {
        String result = this.last == null ? this.url : this.last;
        return result;
    }

    @Override
    public void close() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public int getHttpResponseCode() {
        return httpResponseCode;
    }

    public void setHttpResponseCode(int httpResponseCode) {
        this.httpResponseCode = httpResponseCode;
    }

    public String getHttpResponseMessage() {
        return httpResponseMessage;
    }

    public void setHttpResponseMessage(String httpResponseMessage) {
        this.httpResponseMessage = httpResponseMessage;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
