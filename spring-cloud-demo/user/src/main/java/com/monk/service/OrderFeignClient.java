package com.monk.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@FeignClient(name="SERVER-ORDER")
public interface OrderFeignClient {

    @RequestMapping("/getOrder.do")
    public Map<String, Object> getOrder();
}
