package com.monk.app.propertites;

import org.springframework.boot.autoconfigure.social.SocialProperties;

/**
 * @ClassName WeChatProperties
 * @Description: TODO
 * @Author Monk
 * @Date 2020/5/4
 * @Version V1.0
 **/
public class WeChatProperties extends SocialProperties {

    private String providerId = "wechat";

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
}
