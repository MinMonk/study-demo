package com.monk.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class PowerController {

    private static final Logger logger = LoggerFactory.getLogger(PowerController.class);

    @GetMapping("/getPower.do")
    public Map<String, Object> getPower(String name) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", "200");
        result.put("data", "power");

        // if(null == name){
        //     String msg = "测试服务降级主动抛出的异常";
        //     logger.info(msg);
        //     throw new RuntimeException(msg);
        // }

        // try {
        //     long ms = 1000*10;
        //     logger.info("sleep {}ms", ms);
        //     Thread.sleep(ms);
        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }
        return result;
    }
}
