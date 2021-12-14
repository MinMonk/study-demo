package com.monk.leetcode.structure.list;

public interface CustomList<E> {

    static final int ELEMENT_NOT_FOUND = -1;

    /**
     * 添加元素
     * @param value
     */
    void add(E value);

    /**
     * 向指定位置添加元素
     * @param index
     * @param value
     */
    void add(int index, E value);

    /**
     * 删除指定位置的元素
     * @param index
     * @return
     */
    E remove(int index);

    /**
     * 清空
     */
    void clear();

    /**
     * 修改指定位置的元素值
     * @param index
     * @param value
     * @return
     */
    E set(int index, E value);

    /**
     * 获取指定位置的值
     * @param index
     * @return
     */
    E get(int index);

    /**
     * 返回List集合的大小
     * @return
     */
    int size();

    /**
     * 返回元素在集合中的索引位置
     * @param value
     * @return
     */
    int indexOf(E value);

    boolean isEmpty();

    /**
     * 集合中是否包含指定元素
     * @param value
     * @return
     */
    boolean contains(E value);

}
