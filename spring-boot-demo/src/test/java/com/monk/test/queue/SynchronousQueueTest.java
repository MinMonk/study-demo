package com.monk.test.queue;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SynchronousQueueTest {

    class Producer implements Runnable {
        private SynchronousQueue<String> queue;

        public Producer(SynchronousQueue<String> queue) {
            this.queue = queue;
        }

        private ReentrantLock lock = new ReentrantLock();
        private AtomicInteger num = new AtomicInteger(0);

        @Override
        public void run() {
            while (true) {
                try {
                    lock.lock();
                    String msg = "msg_" + num.getAndIncrement();
                    log.info("thread:{}, write [{}] to queue", Thread.currentThread().getName(), msg);
                    queue.put(msg);

                    // 当总共发送了10条消息的时候，就退出
                    if (num.get() > 10) {
                        System.exit(0);
                    }

                    /**
                     * 生产者每发完一条消息就稍作休息，让消费者线程去消费
                     * 不休息的话，那么通过日志打印会有种put方法可以一下子put两三条消息进去一样的错觉
                     * 而实际上put一条消息，如果这条消息没有线程来take走，那么这一条消息都会一直阻塞住
                     * 
                     * 可以看当前类下的testPutMethod方法
                     * @See com.monk.test.queue.SynchronousQueueTest.testPutMethod()
                     */
                    TimeUnit.MILLISECONDS.sleep(500L);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    class Consumer implements Runnable {
        private SynchronousQueue<String> queue;

        public Consumer(SynchronousQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    log.info("thread:{}, take msg:{}", Thread.currentThread().getName(), queue.take());
                    TimeUnit.MILLISECONDS.sleep(600L);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    private SynchronousQueue<String> queue = new SynchronousQueue<String>();

    
    /**
     * 测试生产者-消费者模式
     * 
     * @author Monk
     * @date 2021年7月7日 下午4:20:07
     */
    @Test
    public void testProducerAndConsumer() {
        new Thread(new Producer(queue), "Producer-1").start();
        new Thread(new Consumer(queue), "Consumer-1").start();
        new Thread(new Consumer(queue), "Consumer-2").start();
        while (true);
    }

    
    /**
     * 执行当前方法，会发现日志只打印了一次，往queue中添加msg_0的时候就阻塞住不在往下执行了
     * 日志输出了start，缺没有输出end
     * 
     * @author Monk
     * @date 2021年7月7日 下午4:20:03
     */
    @Test
    public void testPutMethod() {
        try {
            for (int i = 0; i < 10; i++) {
                String msg = "msg_" + i;
                log.info("thread:{}, write [{}] to queue start", Thread.currentThread().getName(), msg);
                queue.put(msg);
                log.info("thread:{}, write [{}] to queue end", Thread.currentThread().getName(), msg);
            }

        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
    
    /**
     * offer方法不会阻塞队列，但是永远返回的是false
     * 
     * @author Monk
     * @date 2021年7月7日 下午4:23:04
     */
    @Test
    public void testOfferMethod() {
        for (int i = 0; i < 10; i++) {
            String msg = "msg_" + i;
            boolean flag = queue.offer(msg);
            log.info("thread:{}, write [{}] to queue. flag:{}", Thread.currentThread().getName(), msg, flag);
        }
        
        log.info("queue size:{}", queue.size());
    }

}
