package com.monk.leetcode.structure.hash;

import java.util.Objects;

public class LinkedHashMap<K, V> extends HashMap<K, V> {

    private LinkedNode<K, V> first;
    private LinkedNode<K, V> last;

    @Override
    public void clear() {
        super.clear();
        // 清空桶之后,还需要清空带哦first + last指针,否则HashMap中的元素任然有指针在指向引用,那么就不会垃圾回收器回收
        first = last = null;
    }

    @Override
    public boolean containsValue(V value) {
        LinkedNode<K, V> node = first;
        while (node != null) {
            if (Objects.equals(value, node.value)) return true;
            node = node.next;
        }

        return false;
    }

    @Override
    public void traversal(Visitor<K, V> visitor) {
        if (null == visitor) return;

        LinkedNode<K, V> node = first;
        while (node != null) {
            if (visitor.visit(node.key, node.value)) return;
            node = node.next;
        }
    }

    /**
     * 重写创建节点的方法,记录链表的prev + next信息
     *
     * @param key
     * @param value
     * @param parent
     * @return
     */
    @Override
    protected Node<K, V> createNode(K key, V value, Node<K, V> parent) {
        LinkedNode<K, V> node = new LinkedNode(key, value, parent);
        if (first == null) {
            first = last = node;
        } else {
            last.next = node;
            node.prev = last;
            last = node;
        }

        return node;
    }

    /**
     * 由于在删除红黑树上度为2的节点时,实际删除的并不是那个元素,而是叶子节点中找到的后继节点,
     * 在删除的时候,只是将后继节点中的值覆盖了要删除位置的值,但是这两个节点上的prev+next指针的属性并没有修改,
     * 所以在LinkedHashMap中需要修复这两个节点的prev+next指针,将两个节点的位置完成替换
     *
     * @param willNode    将要删除的节点
     * @param removedNode 实际删除的节点
     */
    @Override
    protected void afterRemove(Node<K, V> willNode, Node<K, V> removedNode) {
        LinkedNode<K, V> node1 = (LinkedNode<K, V>) willNode;
        LinkedNode<K, V> node2 = (LinkedNode<K, V>) removedNode;

        /**
         * 1. 处理删除度为2的节点删除
         * 交换两个节点的prev + next指针
         *
         * 这个地方指针的交换,建议自己画图画一下,帮助下理解
         */
        if (node1 != node2) {
            //1. 交换prev指针
            LinkedNode<K, V> temp = node1.prev;
            node1.prev = node2.prev;
            node2.prev = temp;
            if (node1.prev == null) {
                first = node2;
            } else {
                node1.prev.next = node2;
            }

            //1. 交换next指针
            temp = node1.next;
            node1.next = node2.next;
            node2.next = temp;
            if (node2.next == null) {
                last = node2;
            } else {
                node2.next.prev = node1;
            }
        }

        // 处理度 < 2 的节点删除
        LinkedNode<K, V> prev = ((LinkedNode<K, V>) removedNode).prev;
        LinkedNode<K, V> next = ((LinkedNode<K, V>) removedNode).next;
        if (null == prev) {
            first = next;
        } else {
            prev.next = next;
        }

        if (null == next) {
            last = prev;
        } else {
            next.prev = prev;
        }

    }

    /**
     * 链表元素节点
     * 新增prev + next两个属性
     *
     * @param <K>
     * @param <V>
     */
    private static class LinkedNode<K, V> extends Node<K, V> {
        LinkedNode<K, V> prev;
        LinkedNode<K, V> next;

        public LinkedNode(K key, V value, Node<K, V> parent) {
            super(key, value, parent);
        }
    }
}
