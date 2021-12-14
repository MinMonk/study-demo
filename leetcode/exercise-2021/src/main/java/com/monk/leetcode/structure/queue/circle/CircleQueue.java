package com.monk.leetcode.structure.queue.circle;

/**
 * 循环队列
 * @param <E>
 */
public class CircleQueue<E> {

    /* 记录队列队头位置的索引*/
    private int front;
    private int size;
    private E[] elements;
    private static final int DEFAULT_CAPACITY = 10;

    public CircleQueue(){
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

    public void enQueue(E value){
        ensureCapacity(size + 1);

        elements[getIndex(size)] = value;
        size++;
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

    public E deQueue(){
        E top = elements[front];
        elements[front] = null;
        front = getIndex(1);
        size--;
        return top;
    }

    /**
     * 获取下标在数组中的真实索引
     * @param index
     * @return
     */
    private int getIndex(int index) {
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
        index += front;
        return index - (index >= elements.length ? elements.length : 0);
        // return (index + front) % elements.length;
    }

    public E peek(){
        return elements[front];
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
