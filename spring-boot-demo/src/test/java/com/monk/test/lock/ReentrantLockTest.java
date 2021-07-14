package com.monk.test.lock;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2021年7月5日 上午10:01:00
 */
public class ReentrantLockTest {
    
    private static Lock lock = new ReentrantLock();
    private static int sum = 0;

    @Test
    public void test(){
        /**
         * 使用monitorenter 和 monitorexit来实现加锁
         */
        synchronized (this){
            sum++;
        }
    }

    public static void main(String[] args){
        for (int i = 0; i < 3; i++) {
            new Thread(()->{
                try {
                    lock.lock();
                    for (int j = 0; j < 100; j++) {
                        sum++;
                    }
                } finally {
                    lock.unlock();
                }
            }).start();
        }

        try {
            // 休眠1s,让线程执行完成
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(sum);
    }

}
