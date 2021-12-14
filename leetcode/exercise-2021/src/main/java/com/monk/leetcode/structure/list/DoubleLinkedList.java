package com.monk.leetcode.structure.list;

/**
 * 双链链表
 * @param <E>
 */
public class DoubleLinkedList<E> extends CustomAbstractList {

    private Node<E> first;
    private Node<E> last;

    private static class Node<E> {
        private E value;
        private Node<E> prev;
        private Node<E> next;

        public Node(Node<E> prev, E value, Node<E> next) {
            this.prev = prev;
            this.value = value;
            this.next = next;
        }
    }

    public void add(int index, Object value) {

        rangeCheckForAdd(index);

        if (index == size) { // 如果是在最后一个位置添加元素
            Node<E> oldLast = last;
            last = new Node<E>(oldLast, (E)value, null);
            if(oldLast == null){
                //如果最后一个元素为空,说明这是链表添加的第一个元素,那么第一个元素也就是最后一个元素
                first = last;
            }else{
                // 如果最后一个元素不为空,那么还需要将旧last的next指针指向新last
                oldLast.next = last;
            }
        } else {
            Node<E> next = node(index);
            Node<E> prev = next.prev;
            Node<E> newNode = new Node<E>(prev, (E)value, next);
            next.prev = newNode;

            if(prev == null){// 如果插入的位置是第一个
                first = newNode;
            }else{
                prev.next = newNode;
            }
        }

        size++;
    }


    @Override
    public Object remove(int index) {
        rangeCheck(index);

        Node<E> node = node(index);
        Node<E> prev = node.prev;
        Node<E> next = node.next;

        if(prev == null){// 如果删除的元素是链表的第一个元素
            first = next;
        }else{
            prev.next = next;
        }

        if(next== null){// 如果删除的元素是链表的最后一个元素
            last = prev;
        }else{
            next.prev = prev;
        }

        size--;

        return null;
    }

    @Override
    public void clear() {
        size = 0;
        first = null;
    }

    @Override
    public Object set(int index, Object value) {
        Node<E> node = node(index);
        E old = node.value; //记录被修改的值,返回出去
        node.value = (E) value;
        return old;
    }

    @Override
    public E get(int index) {
        return node(index).value;
    }

    @Override
    public int indexOf(Object value) {
        Node<E> node = first;
        if (value == null) {
            for (int i = 0; i < size; i++) {
                if (node.value == null) {
                    return i;
                }
                node = node.next;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (node.value.equals(value)) {
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
            if (i > 0) {
                str.append(", ");
            }
            if(node.prev == null){
                str.append("NULL");
            }else{
                str.append(node.prev.value);
            }
            str.append("_").append(node.value);
            str.append("_");
            if(node.next == null){
                str.append("NULL");
            }else{
                str.append(node.next.value);
            }
            node = node.next;
        }
        str.append("]");

        return str.toString();
    }

    /**
     * 根据索引获取对应位置的元素
     *
     * @param index
     * @return
     */
    private Node<E> node(int index) {
        rangeCheck(index);

        Node<E> node = null;
        if (index > (size >> 1)) {
            // 如果index在中间位置的右侧,就从后往前找
            node = last;
            for (int i = size - 1; i > index; i--) {
                node = node.prev;
            }
        } else {
            // 如果index在中间位置的左侧,就从前往后找
            node = first;
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
        }
        return node;
    }

}
