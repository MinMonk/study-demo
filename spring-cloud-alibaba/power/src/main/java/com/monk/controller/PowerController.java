package com.monk.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class PowerController {

    @GetMapping("/getPower.do")
    @SentinelResource(value = "getPower.do", fallback = "fallbackMethod")
    public Map<String, Object> getPower(String name) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", "200");
        result.put("data", "power");
        if("tom".equals(name)){
            throw new RuntimeException("mock exception:" + name);
        }else if("jack".equals(name)){
            throw new RuntimeException("mock exception:" + name);
        }
        return result;
    }

    public Map<String, Object> fallbackMethod(String name, Throwable throwable) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", "500");
        result.put("data", throwable.getMessage());
        return result;
    }
}
