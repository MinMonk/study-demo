package com.monk.aqs.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockDemo {

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public void read() {
        try {
            lock.readLock().lock();
            System.out.println(Thread.currentThread().getName() + " start.");
            TimeUnit.SECONDS.sleep(2);
            System.out.println(Thread.currentThread().getName() + " end.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void write() {
        try {
            lock.writeLock().lock();
            System.out.println(Thread.currentThread().getName() + " start.");
            TimeUnit.SECONDS.sleep(2);
            System.out.println(Thread.currentThread().getName() + " end.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReentrantReadWriteLockDemo demo = new ReentrantReadWriteLockDemo();
        System.out.println("==========read read============");
        demo.readRead(demo);

        TimeUnit.SECONDS.sleep(5);
        System.out.println("==========read write===========");
        demo.readWrite(demo);

        TimeUnit.SECONDS.sleep(5);
        System.out.println("==========write read===========");
        demo.writeRead(demo);

        TimeUnit.SECONDS.sleep(5);
        System.out.println("==========write write===========");
        demo.writeWrite(demo);
    }

    private void readRead(ReentrantReadWriteLockDemo demo){
        new Thread(() -> {
            demo.read();
        }, "readThread1").start();

        new Thread(() -> {
            demo.read();
        }, "readThread2").start();
    }

    public void readWrite(ReentrantReadWriteLockDemo demo){
        new Thread(() -> {
            demo.read();
        }, "readThread").start();

        new Thread(() -> {
            demo.write();
        }, "writeThread").start();
    }

    public void writeRead(ReentrantReadWriteLockDemo demo){
        new Thread(() -> {
            demo.write();
        }, "writeThread").start();

        new Thread(() -> {
            demo.read();
        }, "readThread").start();
    }

    public void writeWrite(ReentrantReadWriteLockDemo demo){
        new Thread(() -> {
            demo.write();
        }, "writeThread1").start();

        new Thread(() -> {
            demo.write();
        }, "writeThread2").start();
    }

}
