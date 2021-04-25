package com.monk.app.authorize.impl;

import com.monk.app.authorize.AuthorizeConfigProvider;
import com.monk.app.authorize.AuthorizeProviderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MonkAuthorizeProviderManager implements AuthorizeProviderManager {

    @Autowired
    private Set<AuthorizeConfigProvider> authorizeConfigProviderSets;

    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        for(AuthorizeConfigProvider provider : authorizeConfigProviderSets){
            provider.config(config);
        }

        config.anyRequest().authenticated();
    }
}
