package com.monk.leetcode.structure.list;

/**
 * 单链链表
 * @param <E>
 */
public class SingleLinkedList<E> extends CustomAbstractList{

    private Node<E> first;

    private static class Node<E>{
        private E value;
        private Node<E> next;
        public Node(E value, Node<E> next){
            this.value = value;
            this.next = next;
        }
    }

    public void add(int index, Object value) {

        rangeCheckForAdd(index);

        if(size == 0){
            first = new Node<E>((E) value, null);
        }else{
            // 获取插入位置的前一个元素,将元素插入,即使是最后一个元素也无所谓,因为最后一个元素的next=null
            Node<E> prev = node(index - 1);
            prev.next = new Node<E>((E) value, prev.next);
        }

        size++;
    }



    @Override
    public Object remove(int index) {
        rangeCheck(index);

        Node<E> old = first;
        if(index == 0){
            first = first.next;
        }else{
            Node<E> prev = node(index - 1);
            old = prev.next;
            prev.next = old.next;
        }

        size--;

        return old.value;
    }

    @Override
    public void clear() {
        size = 0;
        first = null;
    }

    @Override
    public Object set(int index, Object value) {
        rangeCheck(index);

        Node<E> node = node(index);
        E old = node.value; //记录被修改的值,返回出去
        node.value = (E) value;
        return old;
    }

    @Override
    public Object get(int index) {
        rangeCheck(index);

        Node<E> node = node(index);
        return node.value;
    }

    @Override
    public int indexOf(Object value) {
        Node<E> node = first;
        if(value == null){
            for (int i = 0; i < size; i++) {
                if (node.value == null){
                    return i;
                }
                node = node.next;
            }
        }else{
            for (int i = 0; i < size; i++) {
                if (node.value.equals(value)){
                    return i;
                }
                node = node.next;
            }
        }

        return ELEMENT_NOT_FOUND;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("size: ").append(size).append(", [");
        Node<E> node = first;
        for (int i = 0; i < size; i++) {
            if(i > 0){
                str.append(", ");
            }
            str.append(node.value);
            node = node.next;
        }
        str.append("]");

        return str.toString();
    }

    /**
     * 根据索引获取对应位置的元素
     * @param index
     * @return
     */
    private Node<E> node(int index) {
        rangeCheck(index);
        Node<E> node = first;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return node;
    }

}
