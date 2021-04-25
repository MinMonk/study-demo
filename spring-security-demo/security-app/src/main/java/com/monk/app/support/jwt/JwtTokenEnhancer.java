package com.monk.app.support.jwt;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName JwtTokenEnhancer
 * @Description: TODO
 * @Author Monk
 * @Date 2020/7/5
 * @Version V1.0
 **/
public class JwtTokenEnhancer implements TokenEnhancer {


    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> info = new HashMap<>();
        info.put("enhancer by", "monk");

        ((DefaultOAuth2AccessToken)accessToken).setAdditionalInformation(info);

        return accessToken;
    }
}
