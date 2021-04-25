/**
 * 文件名：NacosWriteableDataSource.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.datasource;

import com.alibaba.csp.sentinel.datasource.WritableDataSource;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 描述：Sentinel持久化到Nacos注册中心数据源 <br/>
 *
 * @author Monk
 * @version V1.0
 * @date 2021年04月13日 10:50
 **/
public class NacosWriteableDataSource implements WritableDataSource {

    public static final Logger logger = LoggerFactory.getLogger(NacosWriteableDataSource.class);

    private String remoteAddress;

    private String groupId;

    private String dataId;

    public NacosWriteableDataSource(String remoteAddress, String groupId, String dataId) {
        this.remoteAddress = remoteAddress;
        this.groupId = groupId;
        this.dataId = dataId;
    }

    @Override
    public void write(Object value) throws Exception {
        String jsonConfig = encodeJson(value);
        ConfigService configService = NacosFactory.createConfigService(remoteAddress);
        boolean isPublishOk = configService.publishConfig(dataId, groupId, jsonConfig);
        logger.info("isPublishOk:{}", isPublishOk);
    }

    @Override
    public void close() throws Exception {

    }

    private <T> String encodeJson(T t) {
        return JSON.toJSONString(t);
    }
}
