package com.monk.test.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2021年7月12日 上午9:40:46
 */
@Slf4j
public class TreadPoolTest {

    @Test
    public void testThreadPool() throws Exception {
        ExecutorService pool = new ThreadPoolExecutor(10, 20, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(20),
                new ThreadFactory() {
                    AtomicInteger atomic = new AtomicInteger();

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "thread-" + atomic.getAndIncrement());
                    }
                }/*, new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        log.error("Task {} rejected from {}", r.toString(), executor.toString());
                        //System.exit(0);
                    }
                 }*/);

        for (int i = 0; i < 40; i++) {
            try {
                pool.execute(new MyTask(i));
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
    }

    class MyTask implements Runnable {

        private int num;

        MyTask(int num) {
            this.num = num;
        }

        @Override
        public void run() {
            // log.info("{} is runing. current message is msg_{}.",
            // Thread.currentThread().getName(), num);
            System.out.println(Thread.currentThread().getName() + ", msg:msg_" + num);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }

        }
    }

}
