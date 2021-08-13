package com.monk.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.monk.properties.ProxyProperties;
import com.monk.service.ProxyService;
import com.monk.service.ProxyServiceFactory;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2021年7月14日 上午9:34:46
 */
@Controller
@Lazy
public class ProxyController {
    
    @Autowired
    private ProxyProperties properties;

    @RequestMapping(value = "/proxy/{proxyType}/**")
    public void proxy(@PathVariable String proxyType, HttpServletRequest request, HttpServletResponse response) {
        try {
            ProxyService proxyService = ProxyServiceFactory.getProxyService(proxyType);
            proxyService.proxy(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }
    
    
    @RequestMapping("/testPro")
    @ResponseBody
    public Map testPro() {
        return properties.getUriMap();
    }
}
