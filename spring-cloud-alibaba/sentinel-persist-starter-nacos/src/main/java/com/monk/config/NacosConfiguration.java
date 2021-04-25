/**
 * 文件名：NacosConfiguration.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.config;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.WritableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.transport.util.WritableDataSourceRegistry;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.monk.datasource.NacosWriteableDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 描述：Nacos持久化配置类 <br/>
 *
 * @author Monk
 * @version V1.0
 * @date 2021年04月13日 14:42
 **/
@Configuration
public class NacosConfiguration {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.cloud.nacos.discovery.server-addr}")
    private String remoteAddress;

    @Value("${spring.cloud.nacos.discovery.group}")
    private String groupId;

    @PostConstruct
    public void loadRules() {
        // 注册 读数据源
        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new NacosDataSource<>(remoteAddress, groupId, appName,
                source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
                }));
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());

        // 注册 写数据源
        WritableDataSource<List<FlowRule>> writableDataSource = new NacosWriteableDataSource(remoteAddress, groupId, appName);
        WritableDataSourceRegistry.registerFlowDataSource(writableDataSource);
    }
}
