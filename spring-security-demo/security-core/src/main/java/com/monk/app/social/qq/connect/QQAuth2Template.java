package com.monk.app.social.qq.connect;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * @ClassName QQAuth2Template
 * @Description: TODO
 * @Author Monk
 * @Date 2020/5/4
 * @Version V1.0
 **/
public class QQAuth2Template extends OAuth2Template {

    public static final Logger logger = LoggerFactory.getLogger(QQAuth2Template.class);

    public QQAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
        setUseParametersForClientAuthentication(true);
    }

    @Override
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
        String responseStr = getRestTemplate().postForObject(accessTokenUrl,parameters, String.class);
        logger.info("获取acessToken的响应为:{}", responseStr);

        /**
         * QQ互联的官方API示例中，这里返回的是String字符串，如下所示：
         *  access_token=FE04************************CCE2&expires_in=7776000&refresh_token=88E4************************BE14
         */
        String[] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(responseStr, "&");
        String accessToken = StringUtils.substringAfterLast(items[0], "=");
        Long expiresIn = Long.valueOf(StringUtils.substringAfterLast(items[1], "="));
        String refreshToken = StringUtils.substringAfterLast(items[2], "=");

        return new AccessGrant(accessToken, null, refreshToken, expiresIn);

    }

    @Override
    protected RestTemplate createRestTemplate() {
        RestTemplate template = super.createRestTemplate();
        template.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return template;
    }
}
