package com.monk.utils.queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class Producer implements Runnable{
    
    private ArrayBlockingQueue<String> queue;
    
    Producer(ArrayBlockingQueue<String> queue){
        this.queue = queue;
    }
    
    private AtomicInteger num = new AtomicInteger(0);

    @Override
    public void run() {
        try {
            while (true) {
                String msg = "msg_" + num.getAndIncrement();
                queue.put(msg);
                log.info("{}往队列中写入了消息{},队列大小:{}", Thread.currentThread().getName(), msg, queue.size());
                // 当总共发送了30条消息的时候，就退出
                if(num.get() > 30) {
                    System.exit(0);
                }
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
}

@Slf4j
class Consumer implements Runnable{
    
    private ArrayBlockingQueue<String> queue;
    
    Consumer(ArrayBlockingQueue<String> queue){
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while(true) {
                String msg = queue.take();
                log.info("{}消费了消息：{}", Thread.currentThread().getName(), msg);
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
    
}


@Slf4j
public class ArrayBlockingQueueTest {

    private final static ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(10);

    /**
     * 测试生产者-消费者模式
     * @author Monk
     * @date 2021年7月5日 下午4:02:06
     */
    @Test
    public void testProducerAndConsumer() throws Exception {
        new Thread(new Producer(queue), "producer_1").start();
        new Thread(new Consumer(queue), "consumer_1").start();
        new Thread(new Consumer(queue), "consumer_2").start();
        while(true);
    }

    @Test
    public void testAddMethod() {
        assertThrows(IllegalStateException.class, () -> {
            for (int i = 0; i < 11; i++) {
                queue.add("msg" + i);
                log.info("msg_{}", i);
            }
        });
    }
    
    /**
     * 当队列满了，会一直阻塞
     * 
     * @author Monk
     * @date 2021年7月5日 下午4:37:59
     */
    @Test
    public void testPutMethod() {
        assertThrows(IllegalStateException.class, () -> {
            for (int i = 0; i < 11; i++) {
                queue.put("msg" + i);
                log.info("msg_{}", i);
            }
        });
    }
    
    /**
     * 阻塞2s后返回失败
     * 
     * @author Monk
     * @date 2021年7月5日 下午4:43:48
     */
    @Test
    public void testOfferMethod() {
        boolean flag = true;
        try {
            for (int i = 0; i < 11; i++) {
                flag = queue.offer("msg" + i, 2, TimeUnit.SECONDS);
                log.info("msg_{}, flag:{}", i, flag);
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        
        assertEquals(false, flag);
    }
    
    /**
     * 测试poll方法，取去队首的元素并返回出来(如果队列为空，则阻塞线程)
     * 
     * @author Monk
     * @date 2021年7月5日 下午4:55:09
     */
    @Test
    public void testTakeMethod() {
        //1. 添加测试数据
        queue.add("msg_1");
        queue.add("msg_2");
        queue.add("msg_3");
        
        try {
            String msg = queue.take();
            log.info("take msg:{}, queue size:{}", msg, queue.size());
            assertEquals("msg_1", msg);
            assertEquals(2, queue.size());
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
    
    
    /**
     * 测试poll方法，取去队首的元素并返回出来(如果队列为空，返回null)
     * 
     * @author Monk
     * @date 2021年7月5日 下午4:54:02
     */
    @Test
    public void testPollMethod() {
        //1. 添加测试数据
        queue.add("msg_1");
        queue.add("msg_2");
        queue.add("msg_3");
        
        String msg = queue.poll();
        log.info("take msg:{}, queue size:{}", msg, queue.size());
        assertEquals("msg_1", msg);
        assertEquals(2, queue.size());
    }
    
    /**
     * 测试删除方法，删除队首的元素并返回出来
     * 
     * @author Monk
     * @date 2021年7月5日 下午4:53:15
     */
    @Test
    public void testRemoveMethod() {
        //1. 添加测试数据
        queue.add("msg_1");
        queue.add("msg_2");
        queue.add("msg_3");
        
        String msg = queue.remove();
        log.info("take msg:{}, queue size:{}", msg, queue.size());
        assertEquals("msg_1", msg);
        assertEquals(2, queue.size());
    }
    
    /**
     * 测试删除一个存在的元素
     * 
     * @author Monk
     * @date 2021年7月5日 下午4:53:15
     */
    @Test
    public void testRemoveExistsMethod() {
        //1. 添加测试数据
        queue.add("msg_1");
        queue.add("msg_2");
        queue.add("msg_3");
        
        boolean flag = queue.remove("msg_2");
        log.info("remove flag:{}, queue size:{}", flag, queue.size());
        assertEquals(true, flag);
        assertEquals(2, queue.size());
    }
    
    /**
     * 测试删除一个不存在的元素
     * 
     * @author Monk
     * @date 2021年7月5日 下午4:53:15
     */
    @Test
    public void testRemoveNotExistsMethod() {
        //1. 添加测试数据
        queue.add("msg_1");
        queue.add("msg_2");
        queue.add("msg_3");
        
        boolean flag = queue.remove("msg_4");
        log.info("remove flag:{}, queue size:{}", flag, queue.size());
        assertEquals(false, flag);
        assertEquals(3, queue.size());
    }
    
    /**
     * 测试peek方法，查询队首的元素，只是查看，并不删除队首的元素
     * 
     * @author Monk
     * @date 2021年7月5日 下午4:56:26
     */
    @Test
    public void testPeekMethod() {
        //1. 添加测试数据
        queue.add("msg_1");
        queue.add("msg_2");
        queue.add("msg_3");
        
        String msg = queue.peek();
        log.info("peek msg:{}, queue size:{}", msg, queue.size());
        assertEquals("msg_1", msg);
        assertEquals(3, queue.size());
    }
    
    @Test
    public void testIteratorMethod() {
        //1. 添加测试数据
        queue.add("msg_1");
        queue.add("msg_2");
        queue.add("msg_3");
        
        Iterator<String> iterator = queue.iterator();
        while(iterator.hasNext()) {
            log.info("iterator msg:{}", iterator.next());
        }
    }
}
