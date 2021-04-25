package com.monk.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class Power2Controller {

    @GetMapping("/getPower.do")
    public Map<String, Object> getPower() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", "200");
        result.put("data", "power2");
        return result;
    }
}
