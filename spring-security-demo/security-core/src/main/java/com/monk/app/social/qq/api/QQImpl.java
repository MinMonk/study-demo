package com.monk.app.social.qq.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.io.IOException;

/**
 * @ClassName QQImpl
 * @Description: TODO
 * @Author Monk
 * @Date 2020/5/3
 * @Version V1.0
 **/
public class QQImpl extends AbstractOAuth2ApiBinding implements QQ {

    private static Logger logger = LoggerFactory.getLogger(QQImpl.class);

    /**
     * 获取OPENID接口请求地址
     */
    private final static String URL_GET_OPENID = "https://graph.qq.com/oauth2.0/me?access_token=%s";

    /**
     * 获取用户信息
     */
    private final static String URL_GET_USERINFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";

    private String appId;

    private String openId;

    private ObjectMapper objectMapper = new ObjectMapper();

    public QQImpl(String appId, String accessToken){
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
        this.appId = appId;

        String url = String.format(URL_GET_OPENID, accessToken);
        String result = getRestTemplate().getForObject(url, String.class);
        this.openId = StringUtils.substringBetween(result, "\"openId\":\"", "\"}");
        logger.info("get OPenId return result:{}, substring after openId:{}", result, openId);
    }

    @Override
    public QQUserInfo getUserInfo() {

        String url = String.format(URL_GET_USERINFO, appId, openId);
        String result = getRestTemplate().getForObject(url, String.class);
        logger.info("get userinfo successful and the return result : [{}]", result);
        QQUserInfo userInfo = null;
        try {
            userInfo = objectMapper.readValue(result, QQUserInfo.class );
            userInfo.setOpenId(openId);
            return userInfo;
        } catch (IOException e) {
           logger.error("Format QQ userInfo failed.", e);
           throw new RuntimeException("Format QQ userInfo failed.", e);
        }
    }
}
