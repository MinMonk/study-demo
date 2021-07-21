package com.monk.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2021年7月14日 上午9:34:46
 */
@Controller
public class ProxyController {

    @RequestMapping(value="/proxy/{type}/**")
    public void proxy2(@PathVariable String proxyType,HttpServletRequest request, HttpServletResponse response) {
        try {
            ProxyService proxy = ProxyService.getProxyService("http://localhost:8082/web2", proxyType);
            proxy.proxy(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }

    }
}
