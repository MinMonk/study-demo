package com.monk.aqs.lock;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {

    private ReentrantLock lock = new ReentrantLock();

    private void a(){
        lock.lock();
        System.out.println("a");
        b();
        lock.unlock();
    }

    private void b() {
        /*try {
            if(!lock.tryLock(1, TimeUnit.SECONDS)){
                System.out.println("1111");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        lock.lock();
        System.out.println("b");
        lock.unlock();
    }

    public static void main(String[] args) {
        ReentrantLockDemo test = new ReentrantLockDemo();
        new Thread(() -> {
            test.a();
        }).start();
    }
}
