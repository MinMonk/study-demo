package com.monk.demo.queue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Customer implements Runnable {

    private volatile boolean isRunning = true;//是否在运行标志
    private BlockingQueue<String> queue;

    public Customer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (isRunning) {

                String data = queue.poll(5, TimeUnit.SECONDS);
                String name = Thread.currentThread().getName();
                System.out.println(name);
                if (null != data) {
                    int random = new Random().nextInt(5);
                    TimeUnit.SECONDS.sleep(random);
                    System.out.println(name + "处理完数据" + data + "数据量=" + queue.size());
                } else {
                    isRunning = false;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
