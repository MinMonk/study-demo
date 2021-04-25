/**
 * 
 * 文件名：HttpCilentUtil.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.common.parsewsdl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Base64;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSessionContext;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * HttpClent获取url数据工具类
 * 
 * @author huangyulan
 * @version V1.0
 * @date 2019年11月25日 上午9:02:16
 */
public class HttpCilentUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpCilentUtil.class);

    /**
     * 默认超时时间
     */
    private static final int DEFAULT_TIMEOUT_SECOND = 60000;

    /**
     * 重定向的Http响应码
     */
    private static final String[] REDIRECT_RESPONSE_CODE = new String[] { "301", "302", "303", "307", "308" };

    /**
     * 想URL发出GET请求
     * 
     * @param url
     *            服务地址
     * @param userName
     *            用户名
     * @param passwd
     *            密码
     * @return 响应结果
     * @throws Exception
     * @author Monk
     * @date 2020年3月31日 下午3:51:49
     */
    public static CloseableHttpResponse executeGetRequestForUrl(String url, String userName, String passwd)
            throws Exception {
        String author = null;
        if (userName != null && passwd != null) {
            author = "Basic " + Base64.getEncoder().encodeToString((userName + ":" + passwd).getBytes());
        }
        CloseableHttpClient httpClient = buildSSLCloseableHttpClient();
        System.setProperty("jsse.enableSNIExtension", "false");
        RequestConfig config = RequestConfig.custom().setConnectTimeout(DEFAULT_TIMEOUT_SECOND)
                .setConnectionRequestTimeout(DEFAULT_TIMEOUT_SECOND).setSocketTimeout(DEFAULT_TIMEOUT_SECOND)
                .build();

        // 创建GET方法的实例
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(config);
        // 设置头信息：如果不设置User-Agent可能会报405，导致取不到数据
        httpGet.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:39.0) Gecko/20100101 Firefox/39.0");
        if (null != author) {
            httpGet.setHeader("Authorization", author);
        }
        CloseableHttpResponse response = httpClient.execute(httpGet);
        return response;
    }

    /**
     * 获取url数据
     * 
     * @param url
     *            服务地址
     * @param userName
     *            用户名
     * @param passwd
     *            密码
     * @return url数据
     * @author huangyulan
     * @date 2019年11月25日 上午9:07:09
     */
    public static InputStream getGetDateByUrl(String url, String userName, String passwd) {
        InputStream inputStream = null;
        CloseableHttpResponse response = null;
        try {
            // 开始执行getMethod
            response = executeGetRequestForUrl(url, userName, passwd);
            int statusCode = response.getStatusLine().getStatusCode();
            logger.info("url:{}, httpCode:{}", url, statusCode);
            if (200 == statusCode) {
                HttpEntity httpEntity = response.getEntity();
                if (null != httpEntity) {
                    String result = EntityUtils.toString(httpEntity, "UTF-8");
                    if (null != result && "" != result) {
                        inputStream = new ByteArrayInputStream(result.getBytes());
                    }
                }
            } else if (Arrays.asList(REDIRECT_RESPONSE_CODE).contains(String.valueOf(statusCode))) {
                Header[] locations = response.getHeaders("Location");
                String location = locations[0].getValue();
                logger.info("Location: [{}]", locations);
                inputStream = getGetDateByUrl(location, userName, passwd);
            }
        } catch (Exception e) {
            inputStream = null;
            logger.error("{}", e);
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return inputStream;
    }

    /**
     * 创建httpclient连接对象（信任所有证书）
     * 
     * @return httpclient连接对象
     * @throws Exception
     * @author huangyulan
     * @date 2019年11月25日 下午6:41:42
     */
    public static CloseableHttpClient buildSSLCloseableHttpClient() throws Exception {
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            // 信任所有
            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        }).build();

        // ALLOW_ALL_HOSTNAME_VERIFIER:这个主机名验证器基本上是关闭主机名验证的,实现的是一个空操作，并且不会抛出javax.net.ssl.SSLException异常。
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
                new String[] { "TLSv1", "TLSv1.1", "TLSv1.2", "SSLv3" }, null, NoopHostnameVerifier.INSTANCE);
        return HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }

}
