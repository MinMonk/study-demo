package com.monk.service.fallback;

import com.monk.service.PowerFeignClient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PowerFeignFallBack implements PowerFeignClient {

    @Override
    public Map<String, Object> getPower() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", "500");
        result.put("data", "默认的降级方法Service");
        return result;
    }
}
