package com.monk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView index(Model model) {
        ModelAndView mv =new ModelAndView();
        mv.addObject("message", "monk");
        mv.setViewName("index");
        return mv;
    }
    
    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello monk1";
    }

}
