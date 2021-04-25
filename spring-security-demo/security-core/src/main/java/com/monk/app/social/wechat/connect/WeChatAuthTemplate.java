package com.monk.app.social.wechat.connect;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName WeChatAuthTemplate
 * @Description: TODO
 * @Author Monk
 * @Date 2020/5/4
 * @Version V1.0
 **/
public class WeChatAuthTemplate extends OAuth2Template {

    public static final Logger logger = LoggerFactory.getLogger(WeChatAuthTemplate.class);

    private String clientId;

    private String clientSecret;

    private String accessTokenUrl;

    private ObjectMapper objectMapper = new ObjectMapper();

    public static final String REFRESH_TOKEN_URL = "https://api.weixin.com/sns/oauth2/refresh_token";

    public WeChatAuthTemplate(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.accessTokenUrl = accessTokenUrl;
        setUseParametersForClientAuthentication(true);
    }

    @Override
    public AccessGrant exchangeForAccess(String authorizationCode, String redirectUri, MultiValueMap<String, String> additionalParameters) {
        StringBuilder accessTokenSRequestUrl = new StringBuilder(accessTokenUrl);
        accessTokenSRequestUrl.append("?appid=" + clientId);
        accessTokenSRequestUrl.append("?secret=" + clientSecret);
        accessTokenSRequestUrl.append("?code=" + authorizationCode);
        accessTokenSRequestUrl.append("?grant_type=authorization_code");
        accessTokenSRequestUrl.append("?redirect_uri=" + redirectUri);

        return getAccessGrant(accessTokenSRequestUrl);
    }

    @Override
    public AccessGrant refreshAccess(String refreshToken, MultiValueMap<String, String> additionalParameters) {
        StringBuilder accessTokenSRequestUrl = new StringBuilder(REFRESH_TOKEN_URL);
        accessTokenSRequestUrl.append("?appid=" + clientId);
        accessTokenSRequestUrl.append("?grant_type=authorization_code");
        accessTokenSRequestUrl.append("?refresh_token=" + refreshToken);

        return getAccessGrant(accessTokenSRequestUrl);
    }

    private AccessGrant getAccessGrant(StringBuilder builder) {
        logger.info("获取access_token的请求地址：{}", builder.toString());
        String responseStr = getRestTemplate().getForObject(builder.toString(), String.class);
        logger.info("获取access_token的响应结果：{}：", responseStr);
        Map<String, Object> responseMap = new HashMap<>();

        try {
            responseMap = objectMapper.readValue(responseStr, Map.class);
        } catch (IOException e) {
            logger.error("Format wechat get access_token response error.", e);
            throw new RuntimeException("Format wechat get access_token response error");
        }

        if (StringUtils.isNotBlank(MapUtils.getString(responseMap, "error_code"))) {
            String errorCode = MapUtils.getString(responseMap, "errcode");
            String errorMsg = MapUtils.getString(responseMap, "errmsg");
            String exceptionMsg = "获取access_token失败。errorCode=%s, errorMsg=%s";
            throw new RuntimeException(String.format(exceptionMsg, errorCode, errorMsg));
        }

        String accessToken = MapUtils.getString(responseMap, "access_token");
        String refreshToken = MapUtils.getString(responseMap, "refresh_token");
        Long expiresIn = MapUtils.getLong(responseMap, "expires_in");
        String scope = MapUtils.getString(responseMap, "scope");
        String openId = MapUtils.getString(responseMap, "openid");
        WeChatAccessGrant accessGrant = new WeChatAccessGrant(accessToken, scope, refreshToken, expiresIn);
        accessGrant.setOpenId(openId);
        return accessGrant;
    }
}
