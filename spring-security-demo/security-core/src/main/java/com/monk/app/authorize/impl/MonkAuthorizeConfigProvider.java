package com.monk.app.authorize.impl;

import com.monk.app.authorize.AuthorizeConfigProvider;
import com.monk.app.constant.SecurityConstant;
import com.monk.app.propertites.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

@Component
public class MonkAuthorizeConfigProvider implements AuthorizeConfigProvider {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        config.antMatchers(
                SecurityConstant.DEFAULT_UN_AUTHENTICATION_URL,
                SecurityConstant.DEFAULT_MOBILE_LOGIN_PROCESSING_URL_FORM,
                "/favicon.ico",
                SecurityConstant.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*",
                securityProperties.getBrowser().getLoginPage(),
                securityProperties.getBrowser().getRegisterPage(),
                SecurityConstant.DEFAULT_REGISTER_URL,
                SecurityConstant.DEFAULT_SIGNOUT_URL,
                securityProperties.getBrowser().getSession().getInvalidSessionUrl() + ".json",
                securityProperties.getBrowser().getSession().getInvalidSessionUrl() + ".html")
                .permitAll();
    }

}
