package com.monk.common.jaxb.cache;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataConfigCache {
    private static Map<String, DataConfig> cache = new ConcurrentHashMap<String, DataConfig>();

    public static String buildCacheKey(String serviceNameEn, String majorVersion){
        return serviceNameEn + "_" + majorVersion;
    }

    public static void put(String key, DataConfig dataConfig) {
        cache.put(key, dataConfig);
    }

    public static void remove(String key) {
        cache.remove(key);
    }

    public static DataConfig get(String key) {
        return cache.get(key);
    }

}
