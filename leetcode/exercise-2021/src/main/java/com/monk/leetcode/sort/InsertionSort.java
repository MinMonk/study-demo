package com.monk.leetcode.sort;

/**
 * 插入排序
 *
 * @param <E>
 */
public class InsertionSort<E extends Comparable> extends Sort<E> {

    @Override
    protected void sort() {
        for (int begin = 1; begin < array.length; begin++) {
            /*for (int i = begin; i > 0; i--) {
                if (compare(i - 1, i) > 0) {
                    swap(i - 1, i);
                }
            }*/

            // 这里虽然两种写法都可以,但是经过测试,while循环的比较以及交换次数是小余for循环方式的
            int curr = begin;
            while (curr > 0 && compare(curr - 1, curr) > 0) {
                swap(curr - 1, curr);
                curr--;
            }
        }
    }
}
