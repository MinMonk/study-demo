package com.monk.test.lock;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CountDownLatchTest {

    /**
     * CountDownLatch锁,会阻塞住线程,直到count数为0的时候,才会去唤醒主线程,让主线程继续
     * 好比一场比赛,10个选手参赛,裁判只有在10个选手都完成了比赛才能宣布比赛结束
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch count = new CountDownLatch(10);

        for (int i = 0; i < 12; i++) {
            new Thread(()->{
                try {
                    TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                    System.out.println(Thread.currentThread().getName() + "完成了比赛.");
                    count.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "player-" + i).start();
        }
        count.await();
        System.out.println("!!!比赛结束!!!");

    }

}
