/**
 * 
 * 文件名：TestThread.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2021年3月23日 下午2:17:56
 */
public class TestThread {

    public static void main(String[] args) {

        testThead();

        Map<String, User> userMap = new HashMap<String, User>();
        userMap.put("userA", new UserA("tom", "A"));
        userMap.put("userB", new UserB("Jerry", "B"));
        System.out.println("1-->" + userMap.get("userA").hashCode());

        UserA a = (UserA) userMap.get("userA");
        a.setUserA("userAA");
        System.out.println("2-->" + userMap.get("userA").hashCode());
        UserB b = (UserB) userMap.get("userB");
        b.setUserB("userBB");

        userMap.put("userA", a);
        userMap.put("userB", b);

        System.out.println("3-->" + userMap.get("userA").hashCode());
        System.out.println(userMap.get("userB"));

    }

    /**
     * 
     * @author Monk
     * @date 2021年3月23日 下午3:22:38
     */
    private static void testThead() {
        String[] users = new String[] { "kangkang", "jack", "Tom", "Rose" };
        CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                try {
                    // 准备完毕……运动员都阻塞在这，等待号令
                    countDownLatch.await();
                    Random rd = new Random();
                    int index = rd.nextInt(4);
                    User user = UserFactory.getSolrClient(users[index]);
                    String parter = "【" + Thread.currentThread().getName() + "】";
                    System.out.println(parter + " " + user + "hashCode:[" + user.hashCode() + "]");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        // 发令枪：执行发令
        countDownLatch.countDown();
    }

}

class UserFactory {
    private static Map<String, User> userMap = new ConcurrentHashMap<String, User>();

    public static User getSolrClient(String name) {
        User user = null;
        synchronized (name) {
            user = userMap.get(name);
            if (null == user) {
                user = userMap.get(name);
                if (null == user) {
                    user = buildUser(name);
                    userMap.put(name, user);
                }
            } else {
                if (!name.equals(user.getName())) {
                    user = buildUser(name);
                    userMap.put(name, user);
                }
            }
        }

        return user;
    }

    private synchronized static User buildUser(String name) {
        User user = null;
        synchronized (name) {
            user = new User(name);
        }
        return user;
    }
}

class User {
    public User(String name) {
        super();
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User [name=" + name + "]";
    }
}

class UserA extends User {
    public UserA(String name, String userA) {
        super(name);
        this.userA = userA;
        initThead();
    }
    
    private ExecutorService  executor = Executors.newFixedThreadPool(10);
    
    private void initThead() {
        executor.execute(new Runnable() {
            
            @Override
            public void run() {
                try {
                    TimeUnit.MINUTES.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String userA;

    public String getUserA() {
        return userA;
    }

    public void setUserA(String userA) {
        this.userA = userA;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    @Override
    public String toString() {
        return "UserA [userA=" + userA + "]";
    }
}

class UserB extends User {
    public UserB(String name, String userB) {
        super(name);
        this.userB = userB;
    }

    private String userB;

    public String getUserB() {
        return userB;
    }

    public void setUserB(String userB) {
        this.userB = userB;
    }

    @Override
    public String toString() {
        return "UserB [userB=" + userB + "]";
    }

}
