package com.monk.leetcode.november;

/**
 * 单链表节点
 */
public class SingleLinkNode {

    private Object value;
    private SingleLinkNode next;

    public SingleLinkNode(Object value){
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public SingleLinkNode getNext() {
        return next;
    }

    public void setNext(SingleLinkNode next) {
        this.next = next;
    }
}
