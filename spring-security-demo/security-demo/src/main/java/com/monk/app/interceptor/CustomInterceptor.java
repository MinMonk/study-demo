package com.monk.app.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomInterceptor implements HandlerInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(CustomInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*String className = ((HandlerMethod)handler).getBean().getClass().getName();
        String methodName = ((HandlerMethod)handler).getMethod().getName();
        logger.info("into custom interceptor preHandle method, and execute the real class is {}, the method is {}", className, methodName);*/
        logger.info("into custom interceptor preHandle method");
        request.setAttribute("name", "tom and jerry");
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Long startTime = (Long) request.getAttribute("startTime");
        Long currTime = System.currentTimeMillis();
        logger.info("into custom interceptor postHandle method, and take {} seconds.", (currTime - startTime));
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Long startTime = (Long) request.getAttribute("startTime");
        Long currTime = System.currentTimeMillis();
        logger.info("into custom interceptor afterCompletion method, and take {} seconds.", (currTime - startTime));
        if(null != ex){
            logger.error("exception is {}", ex);
        }
    }
}
