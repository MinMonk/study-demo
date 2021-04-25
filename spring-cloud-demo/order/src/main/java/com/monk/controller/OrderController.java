package com.monk.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class OrderController {

    @GetMapping("/getOrder.do")
    public Map<String, Object> getOrder() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", "200");
        result.put("data", "order");
        return result;
    }
}
