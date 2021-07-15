package com.monk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2021年7月14日 上午9:34:46
 */
@RequestMapping("/demo")
@Controller
public class IndexController {
    
    @GetMapping("/index.do")
    public String index() {
        return "index";
    }
    
    @GetMapping("/hello")
    @ResponseBody
    public String hello(@RequestParam("name") String name) {
        System.out.println("name:" + name);
        return "hello " + name;
    }
    
    @PostMapping("/postReq")
    @ResponseBody
    public String post(@RequestParam("name") String name) {
        System.out.println("name:" + name);
        return name;
    }

}
