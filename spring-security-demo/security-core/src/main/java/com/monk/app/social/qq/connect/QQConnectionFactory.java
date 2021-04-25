package com.monk.app.social.qq.connect;

import com.monk.app.social.qq.api.QQ;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 * @ClassName QQConnectionFactory
 * @Description: TODO
 * @Author Monk
 * @Date 2020/5/3
 * @Version V1.0
 **/
public class QQConnectionFactory extends OAuth2ConnectionFactory<QQ> {


    /**
     * Create a {@link OAuth2ConnectionFactory}.
     *
     * @param providerId      the provider id e.g. "facebook"
     * @param serviceProvider the ServiceProvider model for conducting the authorization flow and obtaining a native service API instance.
     * @param apiAdapter      the ApiAdapter for mapping the provider-specific service API model to the uniform {@link Connection} interface.
     */
    public QQConnectionFactory(String providerId, String appId, String appSecret) {
        super(providerId, new QQServiceProvider(appId, appSecret), new QQAdapter());
    }
}
