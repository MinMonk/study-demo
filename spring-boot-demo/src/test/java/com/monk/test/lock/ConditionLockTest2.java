package com.monk.test.lock;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock-->condition条件锁联系Demo2
 * 一个线程(DoTask.class)做任务攒经验
 * 一个线程(DoLevel.class)在DoTask线程任务攒够了执行升级操作
 * 目的:借助Condition锁来实现两个线程之间的交替执行
 */
public class ConditionLockTest2 {

    private static Lock lock = new ReentrantLock();

    private static Condition experCon = lock.newCondition();

    private static Condition levelCon = lock.newCondition();

    private static int currLevel = 0;

    public static void main(String[] args) {
        while (currLevel < 10){
            try {
                lock.lock();//加锁是保证线程中的输出打印system.out.print是有序的,而不是线程有序的执行,打印输出的信息是无序的
                new Thread(new DoTask(currLevel*10, (currLevel+1)*10), "task-" + currLevel).start();
                new Thread(new DoLevel(currLevel), "level-" + currLevel).start();
                currLevel++;
            }finally {
                lock.unlock();
            }
            try {
                // 每一次升级休眠一下
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.exit(0);
    }

    static class DoTask implements Runnable{

        DoTask(int value, int max){
            this.experValue = value;
            this.maxExper = max;
        }

        /**
         * 当前等级最大经验值
         */
        private int maxExper;

        /**
         * 当前经验值
         */
        private int experValue;

        @Override
        public void run() {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "初始经验值为" + experValue);
                while (experValue < maxExper){
                    int rad = new Random().nextInt(3) + 1;
                    experValue += rad;
                    System.out.println(Thread.currentThread().getName() + "做任务获得了" + rad + "点经验");
                }
                System.out.println(Thread.currentThread().getName() + "当前等级最大经验值" + maxExper + ", 当前经验值为" + experValue);
                levelCon.signal();//唤醒升级的线程
                experCon.await();//阻塞当前做任务攒经验的线程
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }

        }
    }

    static class DoLevel implements Runnable{

        DoLevel(int level){
            this.level = level;
        }

        private int level;
        @Override
        public void run(){
            try{
                lock.lock();
                level++;
                System.out.println(Thread.currentThread().getName() + "升级了,当前等级为"+level);
                experCon.signal();//升级成功后,唤醒做任务攒经验的线程去做任务攒经验
                levelCon.await();//阻塞当前升级的线程,直到做任务的线程攒够了经验为止
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }

        }
    }
}


