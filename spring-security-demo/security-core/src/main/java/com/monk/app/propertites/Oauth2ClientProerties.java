package com.monk.app.propertites;

/**
 * @ClassName Oauth2ClientProerties
 * @Description: TODO
 * @Author Monk
 * @Date 2020/7/5
 * @Version V1.0
 **/
public class Oauth2ClientProerties {

    private String cliendtId;

    private String clientSecret;

    private int accessTokenValiditySeconds = 3600;

    private int refreshTokenValiditySeconds = 3600 * 24 * 30;

    private String[] grantTypes = new String[]{"password", "authorization_code", "refresh_token"};

    private String[] scopes = new String[]{"all"};

    public String getCliendtId() {
        return cliendtId;
    }

    public void setCliendtId(String cliendtId) {
        this.cliendtId = cliendtId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public int getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    public void setAccessTokenValiditySeconds(int accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }

    public int getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    public void setRefreshTokenValiditySeconds(int refreshTokenValiditySeconds) {
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }

    public String[] getGrantTypes() {
        return grantTypes;
    }

    public void setGrantTypes(String[] grantTypes) {
        this.grantTypes = grantTypes;
    }

    public String[] getScopes() {
        return scopes;
    }

    public void setScopes(String[] scopes) {
        this.scopes = scopes;
    }
}
