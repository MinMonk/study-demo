package com.monk.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;

import com.monk.properties.ProxyProperties;
import com.monk.util.SpringContextHolder;

public class ProxyServiceFactory {
    
    private static Map<String, ProxyService> clientMap = new ConcurrentHashMap<String, ProxyService>();
    
    public static ProxyService getProxyService(String proxyType) throws ServletException {
        ProxyService proxyService = clientMap.get(proxyType);
        if (null != proxyService) {
            return proxyService;
        } else {
            ProxyProperties  properties = SpringContextHolder.getBean("proxyProperties");
            String targetUri = properties.getUriMap().get(proxyType);
            proxyService = new ProxyServiceImpl(targetUri, proxyType);
            clientMap.put(proxyType, proxyService);
        }

        return proxyService;
    }

}
