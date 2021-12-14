package com.monk.leetcode.structure.graph;

import java.util.Collection;
import java.util.Comparator;

/**
 * 小顶堆
 */
public class MinHeap<E> {

    private static final int DEFAULT_CAPACITY = 10;

    private Comparator<E> comparator;

    private E[] elements;

    private int size;

    public MinHeap(Collection<E> data, Comparator<E> comparator){
        this.comparator = comparator;

        this.size = data == null ? 0 : data.size();

        if(size == 0){
            this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        }else{
            size =  Math.max(DEFAULT_CAPACITY,size);
            this.elements = (E[]) new Object[size];
            // copy数据
            int i = 0;
            for (E temp : data) {
                elements[i++] = temp;
            }
        }

        // 批量建堆
        heapify();
    }

    public MinHeap(E[] data, Comparator<E> comparator)  {
        this.comparator = comparator;

        if (data == null || data.length == 0) {
            this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        } else {
            size = data.length;
            int capacity = Math.max(data.length, DEFAULT_CAPACITY);
            this.elements = (E[]) new Object[capacity];
            for (int i = 0; i < data.length; i++) {
                this.elements[i] = data[i];
            }
            heapify();
        }
    }

    public MinHeap(Collection<E> data)  {
        this(data, null);
    }

    public MinHeap(E[] data)  {
        this(data, null);
    }

    public MinHeap(Comparator<E> comparator) {
        this.comparator = comparator;
        this.elements = (E[]) new Object[DEFAULT_CAPACITY];
    }

    public MinHeap(){
        this.elements = (E[]) new Object[DEFAULT_CAPACITY];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    public E get() {
        heapEmptyCheck();

        // 大顶堆的性质就是最大的元素在堆顶也就是第一个元素,所以直接返回索引为0的位置
        return elements[0];
    }

    public void add(E element){
        elementNotNullCheck(element);

        ensureCapacity(size + 1);

        elements[size++] = element;
        siftUp(size - 1);
    }

    public void addAll(Collection<E> data){
        if(data == null) return;
        for(E ele : data){
            add(ele);
        }
    }

    public void addAll(E[] data){
        if(data == null) return;
        for(E ele : data){
            add(ele);
        }
    }

    public E remove() {
        heapEmptyCheck();

        E removedElement = elements[0];
        int lastIdx = --size;
        elements[0] = elements[lastIdx];
        elements[lastIdx] = null;

        siftDown(0);
        return removedElement;
    }

    /**
     * 批量建堆(自下而上的下滤)
     */
    private void heapify() {
        for (int i = (size >> 1) - 1; i >= 0 ; i--) {
            siftDown(i);
        }
    }

    /**
     * 上滤
     *
     * @param index 上滤元素索引
     */
    private void siftUp(int index) {
        E element = elements[index];
        while (index > 0) {
            // 计算父节点的索引
            int parentIdx = (index - 1) >> 1;
            E parent = elements[parentIdx];
            if (compare(element, parent) <= 0) break;

            /**
             * 如果当前节点大于父节点,
             *  1. 拿父节点的值覆盖当前节点
             *  2. 设置当前循环的index为父节点的index
             *  3. 最后找到具体的位置,将当前节点的值直接覆盖上去
             */
            elements[index] = parent;
            index = parentIdx;
        }
        elements[index] = element;
    }

    /**
     * 下滤
     *
     * @param index 下滤元素的索引
     */
    private void siftDown(int index) {
        E element = elements[index];

        /**
         * 找出第一个非叶子节点的索引,因为只有存在子节点的非叶子节点,才有下滤的必要
         * 而根据完全二叉树的特性,可以推出,非叶子节点的数量 = size/2
         * 那么第一个非叶子节点自然就是size/2 + 1,而数组中索引下标是从0开始的,
         * 所以第一个非叶子节点的索引就是Math.floor(size/2),而java的语法,除法
         * 默认就是向下取整,所以第一个非叶子节点的索引就是size/2
         */
        int half = size >> 1;
        while (index < half) {

            /**
             * 能进入循环就说明存在子节点,但是存在子节点分为2种情况:1. 只有左节点; 2. 存在左右两个子节点
             * 而下滤的时候,就是拿父节点和最大的子节点进行比较,并将最大的子节点和父节点进行交换位置
             * 那么这里先默认让最大的子节点为左子节点
             * 而根据完全二叉树的特性,左子节点的索引为left = 2*index + 1,右子节点索引为right = 2*index+2 = left + 1
             */
            int left = (index << 1) + 1;
            int right = left + 1;

            int maxIdx = left; // 这个地方多用一个变量是为了方便后期回看代码,这里可以直接用left
            E maxE = elements[maxIdx];
            E rightE = elements[right];
            if (right < size && compare(rightE, maxE) > 0) {
                maxIdx = right;
                maxE = elements[right];
            }

            // 如果下滤的元素比最大的子节点还要大,那么就退出循环
            if (compare(element, maxE) >= 0) break;

            // 如果下滤的元素比最大的子节点小,那么就需要交换位置,并继续下滤
            elements[index] = maxE;
            index = maxIdx;
        }
        elements[index] = element;
    }

    private int compare(E e1, E e2){
        return comparator != null ? comparator.compare(e1, e2)
                : ((Comparable<E>)e1).compareTo(e2);
    }

    /**
     * 添加的元素是否为空的检查
     *
     * @param element
     */
    private void elementNotNullCheck(E element) {
        if (null == element) {
            throw new IllegalArgumentException("element must not be null.");
        }
    }

    /**
     * 扩容
     *
     * @param capacity 容量
     */
    private void ensureCapacity(int capacity) {
        int oldCapacity = elements.length;

        // 如果当前容量小余之前的容量就不需要扩容
        if (capacity < oldCapacity) return;

        // 扩容后的容量为之前的1.5倍
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        E[] newElements = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements;
    }

    /**
     * 堆是否为空的检查
     */
    private void heapEmptyCheck() {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Heap is empty.");
        }
    }


}
