package com.monk.app.propertites;

import org.springframework.boot.autoconfigure.social.SocialProperties;

/**
 * @ClassName QQProperties
 * @Description: TODO
 * @Author Monk
 * @Date 2020/5/3
 * @Version V1.0
 **/
public class QQProperties extends SocialProperties {
    private String providerId = "qq";

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
}
