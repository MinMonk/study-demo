package com.monk.aqs.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class AqsTest {

    private MyLock lock = new MyLock();

    private int a = 0;

    private int increase(){
        return a++;
    }

    private int increaseAndLock() {
        try {
            lock.lock();
            return a++;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        AqsTest test = new AqsTest();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                System.out.println(test.increase());
            }).start();
        }
    }

    public static class MyLock implements Lock {

        private Helper helper = new Helper();

        private class Helper extends AbstractQueuedSynchronizer {
            @Override
            protected boolean tryAcquire(int arg) {
                int state = getState();
                if (state == 0) {
                    if (compareAndSetState(0, arg)) {
                        setExclusiveOwnerThread(Thread.currentThread());
                        return true;
                    }
                }else if(getExclusiveOwnerThread() == Thread.currentThread()){
                    return true;
                }
                return false;
            }

            @Override
            protected boolean tryRelease(int arg) {
                int state = getState() - arg;
                boolean flag = false;

                if (state == 0) {
                    setExclusiveOwnerThread(null);
                    setState(state);
                    return true;
                }
                setState(state);
                return false;
            }

            private Condition newConditionObject(){
                return new ConditionObject();
            }
        }

        @Override
        public void lock() {
            helper.acquire(1);
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {
            helper.acquireInterruptibly(1);
        }

        @Override
        public boolean tryLock() {
            return helper.tryAcquire(1);
        }

        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            return helper.tryAcquireNanos(1, unit.toNanos(time));
        }

        @Override
        public void unlock() {
            helper.release(1);
        }

        @Override
        public Condition newCondition() {
            return helper.newConditionObject() ;
        }
    }
}
