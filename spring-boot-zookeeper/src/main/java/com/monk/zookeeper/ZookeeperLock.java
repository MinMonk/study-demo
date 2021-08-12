package com.monk.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZookeeperLock implements Lock {
    private CuratorFramework client;
    static {
        // 重连策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

        // 建立客户端
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("192.168.94.200:2181")
                .sessionTimeoutMs(60 * 1000) // 会话超时时间
                .connectionTimeoutMs(5000) // 连接超时时间
                .retryPolicy(retryPolicy).build();
        client.start();

    }

    @Override
    public void lock(String key) {

    }

    @Override
    public boolean tryLock(String key) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void unlock(String key) {
        // TODO Auto-generated method stub

    }

}
