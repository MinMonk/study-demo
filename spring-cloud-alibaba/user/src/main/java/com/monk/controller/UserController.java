package com.monk.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.monk.feign.hystrix.PowerHystrixService;
import com.monk.feign.sentinel.PowerSentinelService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private RestTemplate restTemplate;

    public static final String POWER_URL = "http://service-power";

    @Autowired
    private PowerHystrixService powerHystrixService;

    @Autowired
    private PowerSentinelService powerSentinelService;

    @RequestMapping(value = "/echo/{string}", method = RequestMethod.GET)
    public String echo(@PathVariable String string) {
        return "Hello Nacos Discovery " + string;
    }

    @GetMapping("/getPower.do")
    @HystrixCommand(fallbackMethod = "fallbackMethod")
    @SentinelResource(value = "getPower", fallback = "fallbackMethod")
    public Map<String, Object> getPower(String name) {
        Map<String, Object> result = new HashMap<String, Object>();
        result = restTemplate.getForObject(POWER_URL + "/getPower.do?name="+name, Map.class);
        return result;
    }

    @GetMapping("/getHystrixFeignPower.do")
    public Map<String, Object> getHystrixFeignPower(String name) {
        Map<String, Object> result = new HashMap<String, Object>();
        result = powerHystrixService.getPower(name);
        return result;
    }

    @GetMapping("/getSentinelFeignPower.do")
    public Map<String, Object> getSentinelFeignPower(String name) {
        Map<String, Object> result = new HashMap<String, Object>();
        result = powerSentinelService.getPower(name);
        return result;
    }

    public Map<String, Object> fallbackMethod(String name) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", "500");
        result.put("data", "默认的降级方法Controler");
        return result;
    }
}
