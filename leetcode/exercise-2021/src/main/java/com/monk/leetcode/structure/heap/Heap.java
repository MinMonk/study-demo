package com.monk.leetcode.structure.heap;

public interface Heap<E> {

    /**
     * 元素的数量
     * @return
     */
    int size();

    /**
     * 是否为空
     * @return
     */
    boolean isEmpty();

    /**
     * 清空
     */
    void clear();

    /**
     * 添加元素
     * @param element
     */
    void add(E element);

    /**
     * 获得堆顶元素
     * @return
     */
    E get();

    /**
     * 删除堆顶元素
     * @return
     */
    E remove();

    /**
     * 删除堆顶元素的同时插入一个新元素
     * @param element
     * @return
     */
    E replace(E element);
}
