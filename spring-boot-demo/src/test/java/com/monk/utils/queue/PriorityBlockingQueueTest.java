package com.monk.utils.queue;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Iterator;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class PriorityBlockingQueueTest {

    private PriorityBlockingQueue<String> queue = new PriorityBlockingQueue<String>(10);

    /**
     * 测试add方法超过了队列初始化的长度,是否会抛异常
     * 经过测试,不会抛出异常,会自动扩容
     */
    @Test
    public void testAddMethod() {
        for (int i = 0; i < 11; i++) {
            queue.add("msg_" + i);
            log.info("add msg_{}", i);
        }
        assertEquals(11,queue.size());

        // poll方法从队列中取消息是有序的
        while (queue.size() > 0){
            String msg = queue.poll();
            if(null == msg) continue;
            log.info("poll msg:{}", msg);
        }

        // Iterator从队列中取消息是没有顺序的
        /*Iterator<String> iterator = queue.iterator();
        while(iterator.hasNext()) {
            log.info("iterator read msg:{}", iterator.next());
        }*/

    }

    /**
     * 当队列满了，会一直阻塞
     *
     * @author Monk
     * @date 2021年7月5日 下午4:37:59
     */
    @Test
    public void testPutMethod() {
        for (int i = 0; i < 11; i++) {
            queue.put("msg" + i);
            log.info("msg_{}", i);
        }
        assertEquals(11,queue.size());
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
        for (int i = 0; i < 11; i++) {
            flag = queue.offer("msg" + i, 2, TimeUnit.SECONDS);
            log.info("msg_{}, flag:{}", i, flag);
        }

        assertEquals(11, queue.size());
    }

    /**
     * 测试take方法，取去队首的元素并返回出来(如果队列为空，则阻塞线程)
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
