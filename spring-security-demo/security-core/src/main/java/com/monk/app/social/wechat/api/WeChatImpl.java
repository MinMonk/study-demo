package com.monk.app.social.wechat.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @ClassName WeChatImpl
 * @Description: TODO
 * @Author Monk
 * @Date 2020/5/4
 * @Version V1.0
 **/
public class WeChatImpl extends AbstractOAuth2ApiBinding implements WeChat {

    private static Logger logger = LoggerFactory.getLogger(WeChatImpl.class);

    /**
     * 获取用户信息
     */
    private final static String URL_GET_USERINFO = "https://api.weixin.qq.com/sns/userinfo?openid=";

    private ObjectMapper objectMapper = new ObjectMapper();

    public WeChatImpl(String accessToken) {
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
    }

    @Override
    protected List<HttpMessageConverter<?>> getMessageConverters() {
        /// 默认注册的StringHttpMessageConverter字符集为ISO-8859-1，而微信返回的是UTF-8的，所以覆盖了原来的方法。
        List<HttpMessageConverter<?>> messageConverters = super.getMessageConverters();
        messageConverters.remove(0);
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));

        return messageConverters;
    }

    @Override
    public WeChatUserInfo getUserInfo(String openId) {
        String url = URL_GET_USERINFO + openId;
        String responseStr = getRestTemplate().getForObject(url, String.class);
        if (responseStr.contains("error")) {
            return null;
        }

        WeChatUserInfo userInfo = null;
        try {
            userInfo = objectMapper.readValue(responseStr, WeChatUserInfo.class);
            return userInfo;
        } catch (IOException e) {
            logger.error("Format wechat userInfo failed.", e);
            throw new RuntimeException("Format wechat userInfo failed.", e);
        }
    }
}
