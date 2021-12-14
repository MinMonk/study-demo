package com.monk.leetcode.structure.list;

/**
 * 双链循环链表
 * @param <E>
 */
public class DoubleCircleLinkedList<E> extends CustomAbstractList {

    private Node<E> first;
    private Node<E> last;
    private Node<E> current;

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

    public E next(){
        if(current == null) return null;

        current = current.next;
        return current.value;
    }

    public void reset(){
        current = first;
    }

    public void add(int index, Object value) {

        rangeCheckForAdd(index);

        if (index == size) { // 如果是在最后一个位置添加元素
            Node<E> oldLast = last;
            last = new Node<E>(oldLast, (E)value, first);
            if(oldLast == null){
                //如果最后一个元素为空,说明这是链表添加的第一个元素,那么第一个元素也就是最后一个元素
                first = last;
                first.prev = last;
                first.next = last;
            }else{
                // 如果最后一个元素不为空,那么还需要将旧last的next指针指向新last
                oldLast.next = last;
                first.prev = last;
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

    public Object remove(){
        if(current == null) return null;

        //记录当前元素的下一个元素,方便删除当前元素后,好把下一个元素赋值给当前指针
        Node<E> next = current.next;
        E removed = (E)remove(current);
        // 删除后需要重新判断下链表的大小,如果链表中没有元素,那么current这个指针也没必要往后移动.直接复制为null即可
        if(size == 0){
            current = null;
        }else{
            current = next;
        }

        return removed;
    }

    @Override
    public Object remove(int index) {
        rangeCheck(index);

        Node<E> node = node(index);
        return remove(node);
    }

    private Object remove(Node<E> node) {
        if(size == 1){
            // 如果链表中只有一个元素,删除后就是null
            first = last = null;
        }else{
            Node<E> prev = node.prev;
            Node<E> next = node.next;
            prev.next = next;
            next.prev = prev;

            if(node == first){
                // 如果删除的是第一个元素,那么就将first的指针移动到next上
                first = next;
            }

            if(node == last){
                // 如果删除的元素是最后一个元素,那么就将last的指针移动到prev上
                last = prev;
            }
        }

        size--;
        return node.value;
    }

    @Override
    public void clear() {
        size = 0;
        first = last = null;
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
