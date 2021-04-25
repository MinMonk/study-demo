/**
 * 文件名：ZookeeperWriteDatasource.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.datasource;

import com.alibaba.csp.sentinel.datasource.WritableDataSource;
import com.alibaba.fastjson.JSON;
import com.monk.bean.ZookeeperProperties;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 描述：Zookeeper数据源 <br/>
 *
 * @author Monk
 * @version V1.0
 * @date 2021年04月13日 15:43
 **/
public class ZookeeperWriteDatasource implements WritableDataSource {

    public static final Logger logger = LoggerFactory.getLogger(ZookeeperWriteDatasource.class);

    private ZookeeperProperties zkProperties;

    private CuratorFramework zkClient;

    public ZookeeperWriteDatasource(ZookeeperProperties zkProperties) {
        this.zkProperties = zkProperties;

        zkClient = CuratorFrameworkFactory.newClient(zkProperties.getRemoteAddress(), new ExponentialBackoffRetry(zkProperties.getSleepTime(), zkProperties.getRetryTimes()));
        zkClient.start();
    }

    @Override
    public void write(Object value) throws Exception {
        String jsonConfig = encodeJson(value);

        String path = getPath();
        Stat stat = zkClient.checkExists().forPath(path);
        if (stat == null) {
            zkClient.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, null);
        }
        zkClient.setData().forPath(path, jsonConfig.getBytes());
        logger.info("write to zookeeper successful. configStr:{}", jsonConfig);
    }

    @Override
    public void close() throws Exception {
        zkClient.close();
    }

    private String getPath(){
        return zkProperties.getPrefix() + "/" + zkProperties.getGroupId() + "/" + zkProperties.getDataId();
    }

    private <T> String encodeJson(T t) {
        return JSON.toJSONString(t);
    }
}
