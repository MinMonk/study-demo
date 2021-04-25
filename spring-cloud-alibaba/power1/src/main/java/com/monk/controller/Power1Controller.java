package com.monk.controller;


import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class Power1Controller {

    @GetMapping("/getPower.do")
    public Map<String, Object> getPower(String name) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", "200");
        result.put("data", "power1");
        return result;
    }
}
