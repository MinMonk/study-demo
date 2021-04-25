package com.monk.app.social.wechat.connect;

import com.monk.app.social.wechat.api.WeChat;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.support.OAuth2Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;

/**
 * @ClassName WeChatConnectionFactory
 * @Description: TODO
 * @Author Monk
 * @Date 2020/5/4
 * @Version V1.0
 **/
public class WeChatConnectionFactory extends OAuth2ConnectionFactory<WeChat> {
    public WeChatConnectionFactory(String providerId, String appId, String appSecret) {
        super(providerId, new WeChatServiceProvider(appId, appSecret), new WeChatAdapter());
    }

    @Override
    protected String extractProviderUserId(AccessGrant accessGrant) {
        if (accessGrant instanceof WeChatAccessGrant) {
            return ((WeChatAccessGrant) accessGrant).getOpenId();
        }
        return null;
    }

    @Override
    public Connection<WeChat> createConnection(AccessGrant accessGrant) {
        return new OAuth2Connection<WeChat>(getProviderId(), extractProviderUserId(accessGrant),
                accessGrant.getAccessToken(), accessGrant.getRefreshToken(),
                accessGrant.getExpireTime(), getWeChatServiceProvider(), getApiAdapter());
    }

    @Override
    public Connection<WeChat> createConnection(ConnectionData data) {
        return new OAuth2Connection<WeChat>(data, getWeChatServiceProvider(), getApiAdapter());
    }

    @Override
    public String getProviderId() {
        return super.getProviderId();
    }

    private WeChatServiceProvider getWeChatServiceProvider(){
        return (WeChatServiceProvider) getServiceProvider();
    }

    @Override
    protected ApiAdapter<WeChat> getApiAdapter() {
        return new WeChatAdapter();
    }
}
