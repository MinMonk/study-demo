package com.monk.app.propertites;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName SecurityProperties
 * @Description: TODO
 * @Author Monk
 * @Date 2020/4/5
 * @Version V1.0
 **/
@ConfigurationProperties(prefix = "monk.security")
public class SecurityProperties {

    private AppProperties app = new AppProperties();

    private BrowserProperties browser = new BrowserProperties();

    private ValidateCodeProperties validateCode = new ValidateCodeProperties();

    private SocialProperties social = new SocialProperties();

    private Oauth2Properties oauth2 = new Oauth2Properties();

    public Oauth2Properties getOauth2() {
        return oauth2;
    }

    public void setOauth2(Oauth2Properties oauth2) {
        this.oauth2 = oauth2;
    }

    public SocialProperties getSocial() {
        return social;
    }

    public void setSocial(SocialProperties social) {
        this.social = social;
    }

    public ValidateCodeProperties getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(ValidateCodeProperties validateCode) {
        this.validateCode = validateCode;
    }

    public AppProperties getApp() {
        return app;
    }

    public void setApp(AppProperties app) {
        this.app = app;
    }

    public BrowserProperties getBrowser() {
        return browser;
    }

    public void setBrowser(BrowserProperties browser) {
        this.browser = browser;
    }
}
