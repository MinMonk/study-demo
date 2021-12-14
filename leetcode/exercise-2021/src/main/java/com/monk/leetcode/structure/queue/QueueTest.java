package com.monk.leetcode.structure.queue;


import com.monk.leetcode.structure.queue.circle.CircleDeQueue;
import com.monk.leetcode.structure.queue.circle.CircleQueue;

public class QueueTest {

    public static void main(String[] args) {
        //testCircleQueue();
        testCircleDeQueue();
    }

    /**
     * 测试循环双端队列
     */
    private static void testCircleDeQueue() {
        CircleDeQueue<Integer> queue = new CircleDeQueue<>();

        for (int i = 0; i < 10; i++) {
            queue.enQueueFront(i + 1);
            queue.enQueueRear(i + 100);
        }

        System.out.println(queue);
    }

    /**
     * 测试循环队列
     */
    private static void testCircleQueue() {
        CircleQueue<Integer> queue = new CircleQueue<>();

        for (int i = 0; i < 10; i++) {
            queue.enQueue(i);
        }

        for (int i = 0; i < 5; i++) {
            queue.deQueue();
        }

        for (int i = 20; i < 26; i++) {
            queue.enQueue(i);
        }

        System.out.println(queue);
    }

}
