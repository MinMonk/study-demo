package com.monk.test.redis;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.monk.redis.Lock;
import com.monk.redis.RedisLock;

public class RedisLockTest {

    private int count = 15;

    // private Lock lock = new ReentrantLock();
    private Lock lock = new RedisLock();

    private static ExecutorService pool = new ThreadPoolExecutor(3, 3, 10, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(20), new ThreadFactory() {
                AtomicInteger seq = new AtomicInteger();

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "win-" + seq.getAndIncrement());
                }
            });

    @Test
    public void Test() throws InterruptedException {
        TicketsSaler saler = new TicketsSaler();
        pool.execute(saler);
        pool.execute(saler);
        pool.execute(saler);
        // Thread.currentThread().join();
        while (true)
            ;
    }

    public class TicketsSaler implements Runnable {

        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            while (count > 0) {
                try {
                    lock.lock(name);
                    Random random = new Random();
                    int sec = random.nextInt(1000) + 500;
                    if (name.equals("win-2")) {
                        sec = 5 * 1000;
                    }
                    Thread.sleep(sec);
                    if (count > 0) {
                        System.out.println(
                                Thread.currentThread().getName() + "用时" + sec + "毫秒，售出了第" + count-- + "张票");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock(name);
                }
            }
        }
    }

}
