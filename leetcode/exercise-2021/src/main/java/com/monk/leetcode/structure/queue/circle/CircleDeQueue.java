package com.monk.leetcode.structure.queue.circle;

/**
 * 循环双端队列
 * @param <E>
 */
public class CircleDeQueue<E> {

    /* 记录队列队头位置的索引*/
    private int front;
    private int size;
    private E[] elements;
    private static final int DEFAULT_CAPACITY = 10;

    public CircleDeQueue(){
        elements = (E[])new Object[DEFAULT_CAPACITY];
    }

    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public void clear(){
        for (int i = 0; i < size; i++) {
            elements[getIndex(i)] = null;
        }
        front = 0;
        size = 0;
    }

    /**
     * 从尾部入队
     * @param value
     */
    public void enQueueRear(E value){
        ensureCapacity(size + 1);

        elements[getIndex(size)] = value;
        size++;
    }

    /**
     * 从头部入队
     * @param value
     */
    public void enQueueFront(E value){
        ensureCapacity(size + 1);

        front = getIndex(-1);
        elements[front] = value;
        size++;
    }


    /**
     * 从尾部出队
     * @return
     */
    public E deQueueRear(){
        int rearIndex = getIndex(size - 1);
        E rear = elements[rearIndex];
        elements[rearIndex] = null;

        size--;
        return rear;
    }

    /**
     * 从头部出队
     * @return
     */
    public E deQueueFront(){
        E top = elements[front];
        elements[front] = null;
        front = getIndex(1);
        size--;
        return top;
    }

    /**
     * 查看头部元素
     * @return
     */
    public E peekFirst(){
        return elements[front];
    }

    /**
     * 查看尾部元素
     * @return
     */
    public E peekLast(){
        return elements[getIndex(size - 1)];
    }

    /**
     * 获取下标在数组中的真实索引
     * @param index
     * @return
     */
    private int getIndex(int index) {
        index += front;
        if(index < 0){
            return index + elements.length;
        }

        /**
         * 这个三元运算符,实际上是对取模运算的一个优化,cpu在执行*,/,%这些运算的时候是比较耗性能的
         * 最好换成加减法的运算
         * 但是这样的换算有3个前提:
         *  1. index < 2 * elements.length
         *  2. index >= 0
         *  3. elements.length > 0
         *
         *  总结就是:
         *  已知n >= 0, m > 0 且 n < 2m 可得
         *  n % m = n - (n >= m ? m : 0)
         *
         */
        return index - (index >= elements.length ? elements.length : 0);
        // return index % elements.length;

    }

    /**
     * 扩容
     * @param capacity
     */
    private void ensureCapacity(int capacity) {
        int oldCapacity = elements.length;
        if(capacity > oldCapacity){
            int newCap = oldCapacity + (oldCapacity >> 1);
            E[] newElements = (E[])new Object[newCap];
            for(int i = 0; i < size; i++){
                newElements[i] = elements[getIndex(i)];
            }

            elements = newElements;
            front = 0;
        }
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("capcacity=").append(elements.length)
                .append(" size=").append(size)
                .append(" front=").append(front)
                .append(", [");
        for (int i = 0; i < elements.length; i++) {
            if (i != 0) {
                string.append(", ");
            }

            string.append(elements[i]);
        }
        string.append("]");
        return string.toString();
    }
}
