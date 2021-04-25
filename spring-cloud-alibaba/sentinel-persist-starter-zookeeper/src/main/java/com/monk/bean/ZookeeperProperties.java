/**
 * 文件名：ZookeeperProperties.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 描述：Zookeeper配置 <br/>
 *
 * @author Monk
 * @version V1.0
 * @date 2021年04月13日 15:56
 **/
@ConfigurationProperties("sentinel.persist.zookeeper")
public class ZookeeperProperties {

    private String remoteAddress;

    private String prefix = "/sentinel/config";

    private int retryTimes = 3;

    private int sleepTime = 1000;

    private String groupId;

    private String dataId;

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }
}
