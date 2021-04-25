package com.monk.demo;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class CyclicBarrierDemo {



    public static void main(String[] args) {

        CyclicBarrier cyclicBarrier = new CyclicBarrier(8);

        for (int i = 0; i < 8; i++) {
            new Thread(() -> {
                String name = Thread.currentThread().getName();
                try {
                    TimeUnit.SECONDS.sleep(new Random().nextInt(10));
                    System.out.printf("%s准备好了\r\n", name);
                    cyclicBarrier.await();

                    // 2t69mm

                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("准备就绪，起跑");
            }, "运动员" + i).start();
        }


    }
}
