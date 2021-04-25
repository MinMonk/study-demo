package com.monk.app.propertites;

import com.monk.app.constant.SecurityConstant;

/**
 * @ClassName SessionProperties
 * @Description: TODO
 * @Author Monk
 * @Date 2020/5/17
 * @Version V1.0
 **/
public class SessionProperties {

    private int maximumSessions = 1;

    private Boolean maxSessionsPreventsLogin = false;

    private String invalidSessionUrl = SecurityConstant.SESSION_INVALID_URL;

    public int getMaximumSessions() {
        return maximumSessions;
    }

    public void setMaximumSessions(int maximumSessions) {
        this.maximumSessions = maximumSessions;
    }

    public Boolean getMaxSessionsPreventsLogin() {
        return maxSessionsPreventsLogin;
    }

    public void setMaxSessionsPreventsLogin(Boolean maxSessionsPreventsLogin) {
        this.maxSessionsPreventsLogin = maxSessionsPreventsLogin;
    }

    public String getInvalidSessionUrl() {
        return invalidSessionUrl;
    }

    public void setInvalidSessionUrl(String invalidSessionUrl) {
        this.invalidSessionUrl = invalidSessionUrl;
    }
}
