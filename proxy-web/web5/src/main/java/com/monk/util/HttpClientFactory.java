/**
 * 文件名：HttpClientFactory.java
 * 版权： Copyright 2017-2022 CMCC All Rights Reserved.
 * 描述： ESB管理系统
 */
package com.monk.util;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monk.properties.HttpClientProperties;

/**
 * HttpClient工厂类
 *
 * @author Monk
 * @version V1.0
 * @date 2021年7月24日 下午2:18:45
 */
public class HttpClientFactory {

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(HttpClientFactory.class);

    /**
     * http协议
     */
    private static final String PROTOCOL_HTTP = "http";

    /**
     * https协议
     */
    private static final String PROTOCOL_HTTPS = "https";

    /**
     * 默认超时时间 30s
     */
    private static final int DEFAULT_TIME_OUT = 30 * 1000;

    /**
     * 默认最大连接数：100
     */
    private static int MAX_TOTAL = 100;

    /**
     * 默认每个服务接受的并发请求数：20
     */
    private static int MAX_PER_ROUTE = 20;

    /**
     * 客户端和服务器建立连接的默认连接超时时间
     */
    private static int CONNECT_TIMEOUT = DEFAULT_TIME_OUT;

    /**
     * 客户端从服务器读取数据的默认超时时间
     */
    private static int READ_TIMEOUT = DEFAULT_TIME_OUT;

    /**
     * 默认从连接池获取连接的超时时间：永不超时
     */
    private static int CONNECTION_REQUEST_TIMEOUT = 0;

    /**
     * 默认不允许自动转发
     */
    private static boolean REDIRECTS_ENABLED = false;

    /**
     * SSL连接工厂
     */
    private static SSLConnectionSocketFactory sslConnectionSocketFactory = null;

    /**
     * 连接池管理类
     */
    private static PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = null;

    /**
     * 重试处理
     */
    private static HttpRequestRetryHandler httpRequestRetryHandler = null;

    /**
     * 管理Https连接的上下文类
     */
    private static SSLContextBuilder sslContextBuilder = null;

    /**
     * 请求配置
     */
    private static RequestConfig requestConfig = null;

    /**
     * 初始化配置参数
     * 
     * @author Monk
     * @date 2021年7月24日 下午3:11:01
     */
    private static void initConfig() {
        // 这里是new出来的一个properties，在实际应用中替换成对应配置取值来源即可
        HttpClientProperties properties = SpringContextHolder.getBean("httpClientProperties");
        CONNECT_TIMEOUT = properties.getConnectTimeout();
        READ_TIMEOUT = properties.getReadTimeout();
        CONNECTION_REQUEST_TIMEOUT = properties.getConnectionRequestTimeout();
        MAX_TOTAL = properties.getMaxTotal();
        MAX_PER_ROUTE = properties.getMaxPreRoute();
        REDIRECTS_ENABLED = properties.getRedirectsEnabled();
    }

    /**
     * 初始化HttpClient工厂
     * 
     * @author Monk
     * @date 2021年7月24日 下午2:22:56
     */
    private static synchronized void initHttpClientFactory() {

        if (sslContextBuilder != null && sslConnectionSocketFactory != null && httpRequestRetryHandler != null
                && requestConfig != null && poolingHttpClientConnectionManager != null) {
            return;
        }

        try {
            // 初始化配置参数 全局配置中的配置优先级 高于 当前类中设置的默认值
            initConfig();
            logger.info(
                    "init http client factory. connectTimeout:{}, readTimeout:{}, connectionRequestTimeout:{}, maxTotal:{}, maxPreRoute:{}",
                    new Object[] { CONNECT_TIMEOUT, READ_TIMEOUT, CONNECTION_REQUEST_TIMEOUT, MAX_TOTAL,
                            MAX_PER_ROUTE });

            if (null == sslContextBuilder) {
                buildSSLContext();
            }
            if (null == sslConnectionSocketFactory) {
                buildSocketFactory();
            }
            if (null == httpRequestRetryHandler) {
                buildRetryHandler();
            }
            if (null == requestConfig) {
                buildRequestConfig();
            }
            if (null == poolingHttpClientConnectionManager) {
                buildConnectionManager();
            }

            logger.info("init http client factory end.");
        } catch (NoSuchAlgorithmException e) {
            logger.error("init http client factory error:" + e.getMessage(), e);
        } catch (KeyStoreException e) {
            logger.error("init http client factory error:" + e.getMessage(), e);
        } catch (KeyManagementException e) {
            logger.error("init http client factory error:" + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("init http client factory error:" + e.getMessage(), e);
        }
    }

    /**
     * 构建HttpClient连接池管理对象
     * 
     * @author Monk
     * @date 2021年7月24日 下午4:09:33
     */
    private static void buildConnectionManager() {
        Registry<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory> create()
                .register(PROTOCOL_HTTP, new PlainConnectionSocketFactory())
                .register(PROTOCOL_HTTPS, sslConnectionSocketFactory).build();

        poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(registryBuilder);
        // 设置最大连接数
        poolingHttpClientConnectionManager.setMaxTotal(MAX_TOTAL);
        // 设置每个路由基础的连接增加
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);
    }

    /**
     * 自定义请求配置
     * 
     * @author Monk
     * @date 2021年7月24日 下午4:09:54
     */
    private static void buildRequestConfig() {
        requestConfig = RequestConfig.custom().setRedirectsEnabled(REDIRECTS_ENABLED)
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES).setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(READ_TIMEOUT).setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).build();
    }

    /**
     * 自定义重试处理机制
     * 
     * @author Monk
     * @date 2021年7月24日 下午4:10:08
     */
    private static void buildRetryHandler() {
        httpRequestRetryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                // 如果已经重试了5次，就放弃
                if (executionCount >= 5) {
                    return false;
                }
                // 如果服务器丢掉了连接，那么就重试
                if (exception instanceof NoHttpResponseException) {
                    return true;
                }
                // 不要重试SSL握手异常
                if (exception instanceof SSLHandshakeException) {
                    return false;
                }
                // 超时
                if (exception instanceof InterruptedIOException) {
                    return false;
                }
                // 目标服务器不可达
                if (exception instanceof UnknownHostException) {
                    return false;
                }
                // 连接被拒绝
                if (exception instanceof ConnectTimeoutException) {
                    return false;
                }
                // SSL握手异常
                if (exception instanceof SSLException) {
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };
    }

    /**
     * 构建SSL上下文对象
     * 
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @author Monk
     * @date 2021年7月24日 下午4:10:27
     */
    private static void buildSSLContext() throws NoSuchAlgorithmException, KeyStoreException {
        sslContextBuilder = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                // 信任所有站点 直接返回true
                // 证书管理
                return true;
            }
        });
    }

    /**
     * 构建Socket工厂
     * 
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @author Monk
     * @date 2021年7月24日 下午4:10:50
     */
    private static void buildSocketFactory() throws NoSuchAlgorithmException, KeyManagementException {
        sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContextBuilder.build(),
                new String[] { "SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2" }, null, NoopHostnameVerifier.INSTANCE);
    }

    /**
     * 获取HttpClient对象
     * 
     * @return HttpClient对象
     * @throws Exception
     * @author Monk
     * @date 2021年7月24日 下午2:23:13
     */
    public static CloseableHttpClient getHttpClient() {
        initHttpClientFactory();
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory)
                .setConnectionManager(poolingHttpClientConnectionManager)
                // 设置重试处理器
                .setRetryHandler(httpRequestRetryHandler)
                // 定期清理连接池里的无效连接
                .evictExpiredConnections().useSystemProperties().disableContentCompression()
                .setDefaultRequestConfig(requestConfig).setConnectionManagerShared(true).build();
        return httpClient;
    }

}
