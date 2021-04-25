package com.monk.app.social.qq.config;

import com.monk.app.propertites.QQProperties;
import com.monk.app.propertites.SecurityProperties;
import com.monk.app.social.qq.connect.QQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;

/**
 * @ClassName QQAutoConfig
 * @Description: TODO
 * @Author Monk
 * @Date 2020/5/3
 * @Version V1.0
 **/
@Configuration
@ConditionalOnProperty(prefix = "monk.security.social.qq", name = "app-id")
public class QQAutoConfig extends SocialAutoConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    protected ConnectionFactory<?> createConnectionFactory() {
        QQProperties qqProperties = securityProperties.getSocial().getQq();
        return new QQConnectionFactory(qqProperties.getProviderId(), qqProperties.getAppId(), qqProperties.getAppSecret());
    }
}
