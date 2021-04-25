package com.monk.demo.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Demo {

    public static BlockingQueue queue = new ArrayBlockingQueue(10);

    public static void main(String[] args) {
        int producderNum = 5;
        ExecutorService producers = Executors.newFixedThreadPool(producderNum);
        for (int i = 0; i < producderNum; i++) {
            producers.execute(new Producer(queue));
        }

        int customerNum = 2;
        ExecutorService customers = Executors.newFixedThreadPool(customerNum);
        for (int i = 0; i < customerNum; i++) {
            customers.execute(new Customer(queue));
        }

    }
}
