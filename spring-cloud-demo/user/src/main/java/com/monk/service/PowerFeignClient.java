package com.monk.service;

import com.monk.service.fallback.PowerFeignFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@FeignClient(name="SERVER-POWER", fallback = PowerFeignFallBack.class)
public interface PowerFeignClient {

    @RequestMapping("/getPower.do")
    public Map<String, Object> getPower();
}
