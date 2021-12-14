package com.monk.leetcode.sort;


/**
 * 选择排序
 */
public class SelectionSort<E extends Comparable> extends Sort<E> {

    @Override
    protected void sort() {
        for (int length = array.length - 1; length > 0; length--) {
            int maxIdx = 0;
            for (int i = 0; i <= length; i++) {
                if (compare(i, maxIdx) > 0) {
                    maxIdx = i;
                }
            }
            swap(maxIdx, length);
        }
    }
}
