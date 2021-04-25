package com.monk.demo.queue;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Producer extends Timer implements Runnable {
    private BlockingQueue<String> queue;
    private volatile boolean isRunning = true;//是否在运行标志
    private AtomicInteger integer = new AtomicInteger(1);

    public Producer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        String data = name + "_" + integer.getAndIncrement();
        try {
            while (isRunning) {
                if (!queue.offer(data, 2, TimeUnit.SECONDS)) {
                    this.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            isRunning=false;
                            System.out.println(name+"停止了");
                        }
                    }, 1000);
                    System.out.println("往队列中添加数据失败" + data);
                }
                System.out.println(name + "生产数据完成，queue数据量=" + queue.size());
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
