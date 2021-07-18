package com.monk.test.lock;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock-->condition条件锁联系Demo1
 * 目的:线程之间的交互执行  线程之间的通信
 * 模拟的场景: A犯罪   >>>  B报警 >>>  C接到报警电话   >>>   D处理警务   >>>  E抓到A
 */
public class ConditionLockTest {

    private static Lock lock = new ReentrantLock();

    private static Condition conditionA = lock.newCondition();
    private static Condition conditionB = lock.newCondition();
    private static Condition conditionC = lock.newCondition();
    private static Condition conditionD = lock.newCondition();
    private static Condition conditionE = lock.newCondition();

    private static String current = "A";

    /**
     * A犯罪   >>>  B报警 >>>  C接到报警电话   >>>   D处理警务   >>>  E抓到A
     *
     * @param args
     */
    public static void main(String[] args){

        new Thread(()->{
            try {
                lock.lock();
                while (!current.equals("A")){
                    conditionA.await();
                }

                TimeUnit.SECONDS.sleep(new Random().nextInt(3));
                System.out.println(Thread.currentThread().getName() + "正在违法犯罪");

                current = "B";
                conditionB.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }

        }, "A").start();

        new Thread(()->{
            try {
                lock.lock();
                while (!current.equals("B")){
                    conditionB.await();
                }

                TimeUnit.SECONDS.sleep(new Random().nextInt(3));
                System.out.println(Thread.currentThread().getName() + "拨打了报警电话");

                current = "C";
                conditionC.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }

        }, "B").start();

        new Thread(()->{
            try {
                lock.lock();
                while (!current.equals("C")){
                    conditionC.await();
                }

                TimeUnit.SECONDS.sleep(new Random().nextInt(3));
                System.out.println(Thread.currentThread().getName() + "接到了报警电话");

                current = "D";
                conditionD.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }, "C").start();

        new Thread(()->{
            try {
                lock.lock();
                while (!current.equals("D")){
                    conditionD.await();
                }
                TimeUnit.SECONDS.sleep(new Random().nextInt(3));
                System.out.println(Thread.currentThread().getName() + "正在处理案件");

                current = "E";
                conditionE.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }, "D").start();

        new Thread(()->{
            try {
                lock.lock();
                while (!current.equals("E")){
                    conditionE.await();
                }

                TimeUnit.SECONDS.sleep(new Random().nextInt(3));
                System.out.println(Thread.currentThread().getName() + "正在逮捕嫌疑人");

                current = "A";
                conditionA.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }, "E").start();


    }
}
