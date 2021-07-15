package com.monk.controller;

import java.io.IOException;
import java.util.BitSet;
import java.util.Enumeration;
import java.util.Formatter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.HeaderGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Value("${proxy.mft.targetUri}")
    private String targetUri;

    @RequestMapping(value="/proxy/{type}/**")
    public void proxy2(@PathVariable String type,HttpServletRequest request, HttpServletResponse response) {
        try {
            System.out.println(type);
            ProxyService proxy = ProxyService.getProxyService(targetUri);
            proxy.proxy(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }

    }
}
