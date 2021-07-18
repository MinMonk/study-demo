package com.monk.test.lock;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class CyclicBarrierTest {


    /**
     * 栅栏锁:当前这个例子中模拟的是4人麻将,必须要凑齐4个玩家(线程)才能开始游戏
     * 如果不足4个玩家,将会一直阻塞线程
     * @param args
     */
    public static void main(String[] args) {
        CyclicBarrier cb = new CyclicBarrier(4, ()->{
            System.out.println(Thread.currentThread().getName() + " is ready, let's playing...");
        });

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                try {
                    TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                    System.out.println(Thread.currentThread().getName() + " is ready");
                    cb.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, "player-" + i).start();
        }
    }
}
