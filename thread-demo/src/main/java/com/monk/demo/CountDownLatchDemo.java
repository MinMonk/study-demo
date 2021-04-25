package com.monk.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CountDownLatchDemo {

    private static List<String> companyList = Arrays.asList("东方航空", "南方航空", "深圳航空");

    public static void main(String[] args) throws InterruptedException {
        List<String> queryResult = new ArrayList<>();

        CountDownLatch countDownLatch = new CountDownLatch(companyList.size());

        String origin = "深圳";
        String destination = "哈尔滨";

        CountDownLatchDemo demo = new CountDownLatchDemo();
        for (int i = 0; i < companyList.size(); i++) {
            String companyName = companyList.get(i);
            new Thread(() -> {
                String result = demo.queryFlightTicket(origin, destination);
                queryResult.add(result);
                countDownLatch.countDown();
            }, companyName).start();
        }

        // 唤醒主线程
        countDownLatch.await();
        System.out.println("==========查询结果如下===========");
        queryResult.forEach(System.out::println);
    }

    /**
     * 模拟查询航班余票方法
     *
     * @param origin      出发地
     * @param destination 目的地
     * @return
     */
    private String queryFlightTicket(String origin, String destination) {
        String companyName = Thread.currentThread().getName();
        System.out.printf("%s 正在查询%s到%s的机票...\r\n", companyName, origin, destination);
        int num = new Random().nextInt(10);
        try {
            // 借用休眠来模拟查询的一个过程
            TimeUnit.SECONDS.sleep(num);

            System.out.printf("%s 查询成功\r\n", companyName);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return companyName + "余票：" + num;
    }

}
