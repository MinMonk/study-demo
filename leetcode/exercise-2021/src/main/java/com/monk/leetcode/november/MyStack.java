package com.monk.leetcode.november;

public class MyStack {
    private SingleLinkNode head = null;
    private int size;

    public int getSize() {
        return size;
    }

    /**
     * 向栈中压入值
     * @param value
     */
    public void push(Object value){
        SingleLinkNode node = new SingleLinkNode(value);
        if(head == null){
            head = node;
        }else{
            /**
             * 当head节点不为空的时候,将现在的头节点设置为新节点的下一个节点
             * 再将新节点设置为头节点
             */
            node.setNext(head);
            head = node;
        }
        size++;
    }

    /**
     * 取出栈顶元素
     */
    public Object pop(){
        Object value = head.getValue();
        head = head.getNext();
        size--;
        return value;
    }

    /**
     * 查看栈顶元素(不取去)
     */
    public Object peek(){
        return head.getValue();
    }
}
