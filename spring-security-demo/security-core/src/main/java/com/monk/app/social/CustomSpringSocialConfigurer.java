package com.monk.app.social;

import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * @ClassName CustomSpringSocialConfigurer
 * @Description: TODO
 * @Author Monk
 * @Date 2020/5/4
 * @Version V1.0
 **/
public class CustomSpringSocialConfigurer extends SpringSocialConfigurer {

    private String processesUrl = "";

    public CustomSpringSocialConfigurer(String processesUrl){
        this.processesUrl = processesUrl;
    }

    @Override
    protected <T> T postProcess(T object) {
        SocialAuthenticationFilter authenticationFilter = (SocialAuthenticationFilter) super.postProcess(object);
        authenticationFilter.setFilterProcessesUrl(processesUrl);

        return (T) authenticationFilter;
    }
}
