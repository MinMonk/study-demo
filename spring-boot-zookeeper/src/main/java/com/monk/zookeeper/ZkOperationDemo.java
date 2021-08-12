package com.monk.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ZkOperationDemo {
    
    @Autowired
    private CuratorFramework client;
    
    public void createNode(String node, String value) {
        try {
            client.create().forPath(node, value.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void createNode(String node, String value, long ttl) {
        try {
            client.create().withTtl(ttl).withMode(CreateMode.PERSISTENT_WITH_TTL).forPath(node, value.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void createNode(String node, String value, CreateMode mode) {
        try {
            client.create().withMode(mode).forPath(node, value.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void getNode(String node) {
        try {
            byte[] bytes = client.getData().forPath(node);
            System.out.println(new String(bytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void checkExist(String node) {
        try {
            Stat stat = client.checkExists().forPath(node);
            System.out.println(stat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setValue(String node, String value) {
        try {
            Stat stat = client.setData().forPath(node, value.getBytes());
            System.out.println(stat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void deleteNode(String node) {
        try {
            client.delete().forPath(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
