package com.monk.properties;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("proxyProperties")
public class ProxyProperties implements InitializingBean{

    @Value("${proxy.uri}")
    private String uri;

    @Value("${proxy.doLog}")
    private boolean doLog = true;

    @Value("${proxy.httpClient.doForwardIP}")
    private boolean doForwardIP = true;

    @Value("${proxy.httpClient.doSendUrlFragment}")
    private boolean doSendUrlFragment = true;

    @Value("${proxy.httpClient.doHandleCompression}")
    private boolean doHandleCompression = false;

    @Value("${proxy.httpClient.doPreserveHost}")
    private boolean doPreserveHost = false;

    @Value("${proxy.httpClient.doPreserveCookies}")
    private boolean doPreserveCookies = false;
    
    private Map<String, String> uriMap = new HashMap<String, String>();

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Map<String, String> getUriMap() {
        return uriMap;
    }

    public void setUriMap(Map<String, String> uriMap) {
        this.uriMap = uriMap;
    }

    public boolean isDoLog() {
        return doLog;
    }

    public void setDoLog(boolean doLog) {
        this.doLog = doLog;
    }

    public boolean isDoForwardIP() {
        return doForwardIP;
    }

    public void setDoForwardIP(boolean doForwardIP) {
        this.doForwardIP = doForwardIP;
    }

    public boolean isDoSendUrlFragment() {
        return doSendUrlFragment;
    }

    public void setDoSendUrlFragment(boolean doSendUrlFragment) {
        this.doSendUrlFragment = doSendUrlFragment;
    }

    public boolean isDoHandleCompression() {
        return doHandleCompression;
    }

    public void setDoHandleCompression(boolean doHandleCompression) {
        this.doHandleCompression = doHandleCompression;
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

    @Override
    public String toString() {
        return "ProxyProperties [targetUri=" + uri + ", doLog=" + doLog + ", doForwardIP=" + doForwardIP
                + ", doSendUrlFragment=" + doSendUrlFragment + ", doHandleCompression=" + doHandleCompression
                + ", doPreserveHost=" + doPreserveHost + ", doPreserveCookies=" + doPreserveCookies + "]";
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if(StringUtils.isNotBlank(uri)) {
            String[] uris = uri.split(",");
            for(String str : uris) {
                String[] strs = str.split("=");
                if(strs.length != 2) {
                    throw new RuntimeException("初始化代理配置失败:" + str);
                }
                String type = strs[0];
                String targetUri = strs[1];
                uriMap.put(type, targetUri);
            }
        }
    }

}
