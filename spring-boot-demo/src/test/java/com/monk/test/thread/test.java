package com.monk.test.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 *
 * @author Monk
 * @version V1.0
 * @date 2021年7月12日 上午11:43:39
 */
public class test {
    
    public static void main(String[] args) {
        ExecutorService pool = new ThreadPoolExecutor(10, 20, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10),
                new ThreadFactory() {
            AtomicInteger atomic = new AtomicInteger();

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "thread-" + atomic.getAndIncrement());
            }
        });

        for (int i = 0; i < 100; i++) {
            pool.execute(new MyTask(i));
        }
    }
    
}

class MyTask implements Runnable{
    
    private int num;
    MyTask(int num){
        this.num = num;
    }
    @Override
    public void run() {
        //log.info("{} is runing. current message is msg_{}.", Thread.currentThread().getName(), num);
        System.out.println(Thread.currentThread().getName() + ", msg:msg_" + num);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

