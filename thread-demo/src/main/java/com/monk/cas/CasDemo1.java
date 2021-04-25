package com.monk.cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * compare and swag
 */
public class CasDemo1 {

    private static volatile int a = 0;

    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void increase(){
        a++;
    }

    public static void increase2(){
        atomicInteger.getAndIncrement();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            new Thread(()->{
                CasDemo1.increase();
            }).start();
        }
        System.out.println(a);
        System.out.println("===========================");


        for (int i = 0; i < 20; i++) {
            new Thread(()->{
                CasDemo1.increase2();
            }).start();
        }
        System.out.println(atomicInteger.get());
    }
}
