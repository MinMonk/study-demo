package com.monk.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpMethod;


public class HttpServletRequestProxyFilter implements Filter {

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            if (HttpMethod.POST.name().equalsIgnoreCase(httpServletRequest.getMethod())) {
                requestWrapper = new HttpServletRequestProxyWrapper(httpServletRequest);
            }
        }

        if (requestWrapper == null) {
            chain.doFilter(request, response);
        } else {
            // 替换请求对象
            chain.doFilter(requestWrapper, response);
        }

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }

}
