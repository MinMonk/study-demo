package com.monk.leetcode.sort;

/**
 * 冒泡排序(原始版)
 */
public class BubbleSort<E extends Comparable> extends Sort<E> {

    @Override
    protected void sort() {
        for (int end = array.length - 1; end > 0; end--) {
            for (int begin = 0; begin < end; begin++) {
                /*if (array[begin] > array[begin + 1]) {
                    int temp = array[begin + 1];
                    array[begin + 1] = array[begin];
                    array[begin] = temp;
                }*/
                if (compare(begin, begin + 1) > 0) {
                    swap(begin, begin + 1);
                }
            }
        }
    }
}
