package com.monk.test.thread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池测试类
 * @author Monk
 * @version V1.0
 * @date 2021年7月12日 上午9:40:46
 */
public class TreadPoolTest {

    public static void main(String[] args) throws Exception {
        ExecutorService pool = new ThreadPoolExecutor(10, 20, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(20),
                new ThreadFactory() {
                    AtomicInteger atomic = new AtomicInteger();

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "thread-" + atomic.getAndIncrement());
                    }
                }, new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        System.out.println("reject");
                    }
                 });

        for (int i = 0; i < 50; i++) {
            try {
                pool.execute(new MyTask(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        pool.shutdown();
    }
}

class MyTask implements Runnable {

    private int num;

    MyTask(int num) {
        this.num = num;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ", msg:msg_" + num);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
