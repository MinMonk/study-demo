package com.monk.app.authentication;

import com.monk.app.authentication.mobile.SmCodeAuthenticationSecurityConfig;
import com.monk.app.constant.SecurityConstant;
import com.monk.app.propertites.Oauth2ClientProerties;
import com.monk.app.propertites.SecurityProperties;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName AuthenticationServerConfiguration
 * @Description: TODO
 * @Author Monka
 * @Date 2020/6/17
 * @Version V1.0
 **/
@Configuration
@EnableAuthorizationServer
public class AuthenticationServerConfiguration extends AuthorizationServerConfigurerAdapter{

    public static final Logger logger = LoggerFactory.getLogger(AuthenticationServerConfiguration.class);

    @Autowired
    private TokenStore redisTokenStore;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired(required = false)
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired(required = false)
    private TokenEnhancer jwtTokenEnhancer;


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(redisTokenStore)
                .userDetailsService(userDetailsService)
                .authenticationManager(authenticationManager);

        if(null != jwtAccessTokenConverter && null != jwtTokenEnhancer){
            TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
            List<TokenEnhancer> tokenEnhancerList = new ArrayList<>();
            tokenEnhancerList.add(jwtTokenEnhancer);
            tokenEnhancerList.add(jwtAccessTokenConverter);
            tokenEnhancerChain.setTokenEnhancers(tokenEnhancerList);

            endpoints
                .tokenEnhancer(tokenEnhancerChain)
                .accessTokenConverter(jwtAccessTokenConverter);
        }
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();

        if(ArrayUtils.isNotEmpty(securityProperties.getOauth2().getClients())){
            for(Oauth2ClientProerties proerties : securityProperties.getOauth2().getClients()){
                builder.withClient(proerties.getCliendtId())
                        .secret(proerties.getClientSecret())
                        .accessTokenValiditySeconds(proerties.getAccessTokenValiditySeconds())
                        .refreshTokenValiditySeconds(proerties.getRefreshTokenValiditySeconds())
                        .authorizedGrantTypes(proerties.getGrantTypes())
                        .scopes(proerties.getScopes());
            }
        }else{
            logger.warn("The clients is not config in any proerties.");
        }
    }
}
