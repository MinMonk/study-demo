package com.monk.app.social.qq.connect;

import com.monk.app.social.qq.api.QQ;
import com.monk.app.social.qq.api.QQImpl;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

/**
 * @ClassName QQServiceProvider
 * @Description: TODO
 * @Author Monk
 * @Date 2020/5/3
 * @Version V1.0
 **/
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {

    private static final String URL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";

    private static final String URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";

    private String appId;

    public QQServiceProvider(String appId, String appSecret) {
        super(new QQAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
        this.appId = appId;
    }

    @Override
    public QQ getApi(String accessToken) {
        return new QQImpl(appId, accessToken);
    }
}
