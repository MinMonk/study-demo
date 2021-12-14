package com.monk.leetcode.sort;

/**
 * 插入排序(优化点:减少交换次数,将交换两个元素的位置优化成挪动元素位置)
 *
 * @param <E>
 */
public class InsertionSort1<E extends Comparable> extends Sort<E> {

    @Override
    protected void sort() {
        for (int begin = 1; begin < array.length; begin++) {
            int curr = begin;
            E val = array[begin];//记录当前位置的值,备份一份,以供挪动完位置后,将这个值插入到要插入的位置

            // 和原始的插入排序,这里有点区别,这里是循环比较要插入的值和左边的元素,得到大小后,进行挪动元素
            while (curr > 0 && compare(array[curr - 1], val) > 0) {
                // 当发现当前插入元素小余左边的元素时,就挪动左边的元素到当前位置
                array[curr] = array[curr - 1];
                curr--;
            }
            // 将要插入的元素插入到要插入的位置
            array[curr] = val;
        }
    }
}
