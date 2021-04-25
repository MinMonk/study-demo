package com.monk.app.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;
import java.util.Date;

public class CustomFilter implements Filter {

    private final static Logger logger = LoggerFactory.getLogger(CustomFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("init customer filter, time:{}", new Date());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("customer filter start");
        long startTime = System.currentTimeMillis();
        filterChain.doFilter(servletRequest, servletResponse);
        long endTime = System.currentTimeMillis();
        logger.info("customer filter end. and take {} seconds.", (endTime - startTime));
    }

    @Override
    public void destroy() {
        logger.info("destroy customer filter, time:{}", new Date());
    }
}
