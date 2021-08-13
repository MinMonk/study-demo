package com.monk.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("httpClientProperties")
public class HttpClientProperties {
    
    @Value("${httpClient.connectTimeout}")
    private Integer connectTimeout =30 * 1000;
    
    @Value("${httpClient.readTimeout}")
    private Integer readTimeout =30 * 1000;
    
    @Value("${httpClient.connectionRequestTimeout}")
    private Integer connectionRequestTimeout = 0;
    
    @Value("${httpClient.maxTotal}")
    private Integer maxTotal = 100;
    
    @Value("${httpClient.maxPreRoute}")
    private Integer maxPreRoute = 20;
    
    @Value("${httpClient.redirectsEnabled}")
    private Boolean redirectsEnabled = false;

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
    }

    public Integer getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(Integer connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public Integer getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public Integer getMaxPreRoute() {
        return maxPreRoute;
    }

    public void setMaxPreRoute(Integer maxPreRoute) {
        this.maxPreRoute = maxPreRoute;
    }

    public Boolean getRedirectsEnabled() {
        return redirectsEnabled;
    }

    public void setRedirectsEnabled(Boolean redirectsEnabled) {
        this.redirectsEnabled = redirectsEnabled;
    }

    @Override
    public String toString() {
        return "HttpClientProperties [connectTimeout=" + connectTimeout + ", readTimeout=" + readTimeout
                + ", connectionRequestTimeout=" + connectionRequestTimeout + ", maxTotal=" + maxTotal
                + ", maxPreRoute=" + maxPreRoute + ", redirectsEnabled=" + redirectsEnabled + "]";
    }

}
