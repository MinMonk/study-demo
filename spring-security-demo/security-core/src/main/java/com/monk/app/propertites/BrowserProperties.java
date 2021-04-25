package com.monk.app.propertites;

import com.monk.app.constant.SecurityConstant;
import com.monk.app.validate.bean.LoginResponseType;

/**
 * @ClassName WebProperties
 * @Description: TODO
 * @Author Monk
 * @Date 2020/4/5
 * @Version V1.0
 **/
public class BrowserProperties {

    private String loginPage = SecurityConstant.DEFAULT_LOGIN_PAGE;

    private String signOutPage;

    private String registerPage = SecurityConstant.DEFAULT_REGISTER_PAGE;

    private LoginResponseType loginType = LoginResponseType.JSON;

    private int rememberedSeconds = 3600;

    private SessionProperties session = new SessionProperties();

    public int getRememberedSeconds() {
        return rememberedSeconds;
    }

    public void setRememberedSeconds(int rememberedSeconds) {
        this.rememberedSeconds = rememberedSeconds;
    }

    public String getRegisterPage() {
        return registerPage;
    }

    public void setRegisterPage(String registerPage) {
        this.registerPage = registerPage;
    }

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public String getSignOutPage() {
        return signOutPage;
    }

    public void setSignOutPage(String signOutPage) {
        this.signOutPage = signOutPage;
    }

    public LoginResponseType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginResponseType loginType) {
        this.loginType = loginType;
    }

    public SessionProperties getSession() {
        return session;
    }

    public void setSession(SessionProperties session) {
        this.session = session;
    }
}
