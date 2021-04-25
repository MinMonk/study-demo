package com.monk.feign.hystrix;

import com.monk.feign.hystrix.PowerHystrixService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PowerHystrixFallBack implements PowerHystrixService {

    @Override
    public Map<String, Object> getPower(String name) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", "500");
        result.put("data", "Hystrix默认的降级方法Service");
        return result;
    }
}
