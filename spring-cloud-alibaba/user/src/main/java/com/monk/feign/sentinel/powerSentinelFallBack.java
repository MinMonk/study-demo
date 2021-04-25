package com.monk.feign.sentinel;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class powerSentinelFallBack implements PowerSentinelService{

    @Override
    public Map<String, Object> getPower(String name) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", "500");
        result.put("data", "sentinel默认的降级方法Service");
        return result;
    }
}
