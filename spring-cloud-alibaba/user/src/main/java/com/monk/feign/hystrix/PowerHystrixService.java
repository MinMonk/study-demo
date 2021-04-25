package com.monk.feign.hystrix;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@FeignClient(name="service-power", fallback = PowerHystrixFallBack.class)
public interface PowerHystrixService {

    @RequestMapping("/getPower.do")
    public Map<String, Object> getPower(String name);
}
