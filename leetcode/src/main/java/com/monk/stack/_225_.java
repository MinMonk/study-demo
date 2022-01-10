package com.monk.stack;

import java.util.LinkedList;
import java.util.Queue;

/**
 * https://leetcode-cn.com/problems/implement-stack-using-queues/
 *
 * 225. 用队列实现栈
 */
public class _225_ {


    public static void main(String[] args) {
        _225_ stack = new _225_();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);

        while (!stack.empty()){
            System.out.println(stack.pop());
        }

        System.out.println( Math.ceil((double)7/ 2));
        System.out.println(7 >> 1);
    }

    private Queue<Integer> queue;

    public _225_() {
        queue = new LinkedList<Integer>();
    }

    /**
     * 每一次往队列中添加元素的时候,依次弹出队列中已经存在的顶部元素,再添加到队列的尾部
     * @param x
     */
    public void push(int x) {
        int size = queue.size();
        queue.offer(x);
        for (int i = 0; i < size; i++) {
            queue.offer(queue.poll());
        }
    }

    public int pop() {
        return queue.poll();
    }

    public int top() {
        return queue.peek();
    }

    public boolean empty() {
        return queue.isEmpty();
    }
}
