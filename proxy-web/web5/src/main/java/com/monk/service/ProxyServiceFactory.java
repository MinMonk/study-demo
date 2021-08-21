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
            /**
             * <pre>
             *     这个地方有个隐藏的问题需要留意一下:
             *      如果这个targetUri是会在上线后改变的话,那么这里的缓存map就会有问题,缓存是根据proxyType为key
             *      对应的proxyService实例对象为value,而ProxyService是根据targetUri来实例化的,所以如果这个
             *      targetUri会发生改变,那么这里就要做相对应的控制
             *     比方说:
             *      方案一:每次拿到缓存对象的时候,将对象中的targetUri与当前最新的targetUri进行对比,如果不一致,就重新实例化
             *      方案二:给targetUri添加改变时间,当targetUri发生改变的时候,就将缓存map中这个proxyType对象的值删掉,让他改变后重新实例化再put进去
             *
             *
             * </pre>
             */
            String targetUri = properties.getUriMap().get(proxyType);
            proxyService = new ProxyServiceImpl(targetUri, proxyType);
            clientMap.put(proxyType, proxyService);
        }

        return proxyService;
    }

}
