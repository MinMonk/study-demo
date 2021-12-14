package com.monk.leetcode.structure.queue;


import com.monk.leetcode.structure.list.CustomList;
import com.monk.leetcode.structure.list.DoubleLinkedList;

public class MyQueue<E> {

    private CustomList<E> list = new DoubleLinkedList();

    public int size(){
        return list.size();
    }

    public boolean isEmpty(){
        return list.isEmpty();
    }

    public void clear(){
        list.clear();
    }

    public void enQueue(E value){
        list.add(value);
    }

    public E deQueue(){
        return list.remove(0);
    }

    public E peek(){
        return list.get(0);
    }

}
