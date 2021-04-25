package com.monk.app.authentication;

import com.monk.app.constant.SecurityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class AbstractChannelSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    protected AuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    protected AuthenticationFailureHandler customAuthenticationFailedHandler;

    protected void applyPasswordAuthenticationConfig(HttpSecurity http) throws Exception{
        http.formLogin()
            .loginPage(SecurityConstant.DEFAULT_UN_AUTHENTICATION_URL)
            .loginProcessingUrl(SecurityConstant.DEFAULT_LOGIN_PROCESSING_URL_FORM)
            .successHandler(customAuthenticationSuccessHandler)
            .failureHandler(customAuthenticationFailedHandler);
    }

}
