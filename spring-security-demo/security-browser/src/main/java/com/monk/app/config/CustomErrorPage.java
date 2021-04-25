package com.monk.app.config;

import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ErrorPageRegistrar;
import org.springframework.boot.web.servlet.ErrorPageRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @ClassName CustomErrorPage
 * @Description: TODO
 * @Author Monk
 * @Date 2020/5/3
 * @Version V1.0
 **/
@Component
public class CustomErrorPage implements ErrorPageRegistrar {

    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        ErrorPage[] errorPage = new ErrorPage[3];
        errorPage[0] = new ErrorPage(HttpStatus.NOT_FOUND, "/error/404.html");
        errorPage[1] = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500.html");
        errorPage[2] = new ErrorPage(HttpStatus.METHOD_NOT_ALLOWED, "/error/405.html");
        registry.addErrorPages(errorPage);
    }
}
