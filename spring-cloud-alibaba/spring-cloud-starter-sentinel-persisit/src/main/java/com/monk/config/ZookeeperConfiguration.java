/**
 * 文件名：ZookeeperConfiguration.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.config;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.WritableDataSource;
import com.alibaba.csp.sentinel.datasource.zookeeper.ZookeeperDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.transport.util.WritableDataSourceRegistry;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.monk.bean.ZookeeperProperties;
import com.monk.datasource.ZookeeperWriteDatasource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 描述：Zookeeper配置类 <br/>
 *
 * @author Monk
 * @version V1.0
 * @date 2021年04月13日 15:34
 **/
@Configuration
@EnableConfigurationProperties(ZookeeperProperties.class)
public class ZookeeperConfiguration {

    @Autowired
    private ZookeeperProperties zkProperties;

    @PostConstruct
    public void loadRules() {
        String path = getPath();
        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new ZookeeperDataSource<>(zkProperties.getRemoteAddress(), path,
                source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {}));
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());

        // 注册 写数据源
        WritableDataSource<List<FlowRule>> writableDataSource = new ZookeeperWriteDatasource(zkProperties);
        WritableDataSourceRegistry.registerFlowDataSource(writableDataSource);

    }

    private String getPath(){
        return zkProperties.getPrefix() + "/" + zkProperties.getGroupId() + "/" + zkProperties.getDataId();
    }
}
