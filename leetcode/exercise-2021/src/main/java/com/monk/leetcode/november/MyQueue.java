package com.monk.leetcode.november;

public class MyQueue {
    private SingleLinkNode head = null;
    private SingleLinkNode tail = null;
    private int size;

    public int getSize() {
        return size;
    }

    /**
     * 入队,向队列中添加元素
     * @param value
     */
    public void enQueue(Object value){
        SingleLinkNode node = new SingleLinkNode(value);
        if(head == null){
            head = tail = node;
        }else{
            tail.setNext(node);
            tail = node;
        }
        size++;
    }

    /**
     * 出队,从队列中取出元素
     */
    public Object deQueue(){
        Object value = head.getValue();
        head = head.getNext();
        size--;
        return value;
    }

    /**
     * 查看队列中的第一个元素
     */
    public Object peek(){
        return head.getValue();
    }
}
