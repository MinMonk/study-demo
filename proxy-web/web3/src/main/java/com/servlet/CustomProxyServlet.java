package com.servlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import org.mitre.dsmiley.httpproxy.URITemplateProxyServlet;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2021年7月14日 下午4:38:26
 */
@WebServlet(name = "customProxy", urlPatterns = { "/proxy/*" }, initParams = {
        @WebInitParam(name = "targetUri", value = "http://localhost:8082/web2/"),
        @WebInitParam(name = "log", value = "true") })
public class CustomProxyServlet extends URITemplateProxyServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -2613983470370393178L;

}
