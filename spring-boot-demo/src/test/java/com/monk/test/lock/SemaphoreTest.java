package com.monk.test.lock;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 信号量锁
 *
 * 保证资源同时只能有3个线程可以访问,其他线程必须等待占用资源的线程释放了资源才可以继续访问
 * @author Monk
 * @version V1.0
 * @date 2021年7月8日 下午4:00:13
 */
public class SemaphoreTest {

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3, true);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + " 获得了信号量");
                    TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    semaphore.release();
                }
                
            }, "thread-" + i).start();
        }
        
        while(true);
    }

}
