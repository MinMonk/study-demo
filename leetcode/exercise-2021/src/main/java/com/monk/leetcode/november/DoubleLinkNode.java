package com.monk.leetcode.november;

/**
 * 双链表节点
 */
public class DoubleLinkNode {

    private Object value;
    private DoubleLinkNode left;
    private DoubleLinkNode right;

    public DoubleLinkNode(Object value){
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public DoubleLinkNode getLeft() {
        return left;
    }

    public void setLeft(DoubleLinkNode left) {
        this.left = left;
    }

    public DoubleLinkNode getRight() {
        return right;
    }

    public void setRight(DoubleLinkNode right) {
        this.right = right;
    }
}
