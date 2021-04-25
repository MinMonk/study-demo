package com.monk.app.social.wechat.connect;

import com.monk.app.social.wechat.api.WeChat;
import com.monk.app.social.wechat.api.WeChatImpl;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

/**
 * @ClassName WeChatServiceProvider
 * @Description: TODO
 * @Author Monk
 * @Date 2020/5/4
 * @Version V1.0
 **/
public class WeChatServiceProvider extends AbstractOAuth2ServiceProvider<WeChat> {

    /**
     * 微信获取授权码的url
     */
    private static final String URL_AUTHORIZE = "https://open.weixin.qq.com/connect/qrconnect";
    /**
     * 微信获取accessToken的url
     */
    private static final String URL_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";

    public WeChatServiceProvider(String appId, String appSecret) {
        super(new WeChatAuthTemplate(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
    }

    @Override
    public WeChat getApi(String accessToken) {
        return new WeChatImpl(accessToken);
    }
}
