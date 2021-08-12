package com.monk.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "zookeeper.curator")
public class ZookeeperProperties {

    private String connectString;

    private int connectionTimeout;
    
    private int sessionTimeout;
    
    private int retryCount;

    private int elapsedTimeMs;

    public String getConnectString() {
        return connectString;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public int getElapsedTimeMs() {
        return elapsedTimeMs;
    }

    public void setElapsedTimeMs(int elapsedTimeMs) {
        this.elapsedTimeMs = elapsedTimeMs;
    }

    @Override
    public String toString() {
        return "ZookeeperProperties [connectString=" + connectString + ", retryCount=" + retryCount
                + ", connectionTimeout=" + connectionTimeout + ", sessionTimeout=" + sessionTimeout
                + ", elapsedTimeMs=" + elapsedTimeMs + "]";
    }

}
