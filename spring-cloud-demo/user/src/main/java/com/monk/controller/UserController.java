package com.monk.controller;


import com.monk.service.OrderFeignClient;
import com.monk.service.PowerFeignClient;
import com.netflix.discovery.converters.Auto;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    public static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private PowerFeignClient powerFeignClient;

    private static final String POWER_URL = "http://SERVER-POWER";
    private static final String ORDER_URL = "http://SERVER-ORDER";

    @GetMapping("/getUser.do")
    public Map<String, Object> getUser() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", "200");
        result.put("data", "user");
        return result;
    }

    @GetMapping("/getPower.do")
    @HystrixCommand(fallbackMethod = "fallbackMethod")
    public Map<String, Object> getPower(){
        Map<String, Object> result = new HashMap<String, Object>();
        logger.info("调用了getPower方法");
        // result = restTemplate.getForObject(POWER_URL + "/getPower.do", Map.class);
        result = powerFeignClient.getPower();
        return result;
    }

    @GetMapping("/getOrder.do")
    public Map<String, Object> getOrder(){
        Map<String, Object> result = new HashMap<String, Object>();
        //result = restTemplate.getForObject(ORDER_URL + "/getOrder.do", Map.class);
        result = orderFeignClient.getOrder();
        return result;
    }

    // 注解中的值会覆盖application.yml中的配置
    @GetMapping("/getFeignPower.do")
    @HystrixCommand(fallbackMethod = "fallbackMethod", threadPoolKey = "power",
    threadPoolProperties = {
            @HystrixProperty(name="coreSize", value = "5")
    })
    public Map<String, Object> getFeignPower(){
        Map<String, Object> result = new HashMap<String, Object>();
        logger.info("调用了getFeignPower方法");
        result = powerFeignClient.getPower();
        // result = restTemplate.getForObject(POWER_URL + "/getPower.do", Map.class);
        return result;
    }

    private Map<String, Object> fallbackMethod() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", "500");
        result.put("data", "默认的降级方法Controler");
        return result;
    }
}
