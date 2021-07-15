package com.monk.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String index(Model model, HttpServletRequest request) {
        String uri = request.getRequestURI();
        String url = request.getRequestURL().toString();
        String pathInfo = request.getPathInfo();
        String contextPath = request.getContextPath();
        model.addAttribute("uri", uri);
        model.addAttribute("url", url);
        model.addAttribute("pathInfo", pathInfo);
        model.addAttribute("contextPath", contextPath);
        return "index";
    }
    
    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello monk3";
    }

}
