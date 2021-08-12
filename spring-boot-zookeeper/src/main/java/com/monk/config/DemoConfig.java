package com.monk.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.monk.properties.ZookeeperProperties;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2021年6月30日 上午10:37:10
 */
@Configuration
@ComponentScan("com.monk")
public class DemoConfig {

    @Autowired
    private ZookeeperProperties properties;

    @Bean(initMethod = "start")
    public CuratorFramework curatorFramework() {
        // 重连策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(properties.getElapsedTimeMs(),
                properties.getRetryCount());

        // 建立客户端
        CuratorFramework zkClient = CuratorFrameworkFactory.builder().connectString(properties.getConnectString())
                .sessionTimeoutMs(properties.getSessionTimeout()) // 会话超时时间
                .connectionTimeoutMs(properties.getConnectionTimeout()) // 连接超时时间
                .retryPolicy(retryPolicy).build();
        return zkClient;
    }

}
