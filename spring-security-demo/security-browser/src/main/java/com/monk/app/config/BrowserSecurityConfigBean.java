package com.monk.app.config;

import com.monk.app.propertites.SecurityProperties;
import com.monk.app.session.MonkCustomSessionExpiredStategry;
import com.monk.app.session.MonkCustomSessionInvalidStrategy;
import com.monk.app.signout.MonkLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

/**
 * @ClassName BrowserSecurityConfigBean
 * @Description: TODO
 * @Author Monk
 * @Date 2020/5/17
 * @Version V1.0
 **/
@Configuration
public class BrowserSecurityConfigBean {

    @Autowired
    private SecurityProperties securityProperties;

    @Bean
    @ConditionalOnMissingBean(InvalidSessionStrategy.class)
    public InvalidSessionStrategy invalidSessionStrategy(){
        return new MonkCustomSessionInvalidStrategy(securityProperties.getBrowser().getSession().getInvalidSessionUrl());
    }

    @Bean
    @ConditionalOnMissingBean(SessionInformationExpiredStrategy.class)
    public SessionInformationExpiredStrategy sessionInformationExpiredStrategy(){
        return new MonkCustomSessionExpiredStategry(securityProperties.getBrowser().getSession().getInvalidSessionUrl());
    }

    @Bean
    @ConditionalOnMissingBean(LogoutSuccessHandler.class)
    public LogoutSuccessHandler LogoutSuccessHandler(){
        return new MonkLogoutSuccessHandler(securityProperties.getBrowser().getSignOutPage());
    }

}
