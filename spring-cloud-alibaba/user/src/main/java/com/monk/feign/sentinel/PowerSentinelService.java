package com.monk.feign.sentinel;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@FeignClient(name="service-power", fallback = powerSentinelFallBack.class)
public interface PowerSentinelService {

    @RequestMapping("/getPower.do")
    public Map<String, Object> getPower(String name);
}
