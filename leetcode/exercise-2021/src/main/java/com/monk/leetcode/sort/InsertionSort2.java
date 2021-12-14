package com.monk.leetcode.sort;

/**
 * 插入排序
 * 优化点:
 * 1. 减少交换次数,将交换两个元素的位置优化成挪动元素位置
 * 2. 减少比较的次数,用二叉查找找到要挪动的一批元素
 *
 * @param <E>
 */
public class InsertionSort2<E extends Comparable> extends Sort<E> {

    @Override
    protected void sort() {
        for (int begin = 1; begin < array.length; begin++) {
            insert(begin, search(begin));
        }
    }

    /**
     * 将source位置的元素插入到dest位置
     *
     * @param source
     * @param dest
     */
    private void insert(int source, int dest) {
        E val = array[source];
        // 从source位置的元素开始挪动,挪动到dest位置为止
        for (int i = source; i > dest; i--) {
            array[i] = array[i - 1];
        }
        // 将source位置的值插入到dest位置
        array[dest] = val;
    }

    /**
     * 用二叉查找法找出要插入的位置
     *
     * @param index
     * @return
     */
    private int search(int index) {
        int begin = 0;
        int end = index;
        while (begin < end) {
            int mid = (begin + end) >> 1;
            if (compare(mid, index) > 0) {
                // 如果index位置的值小余mid位置的值,就修改end的值为mid
                end = mid;
            } else {
                // 反之就修改begin的值为mid+1
                begin = mid + 1;
            }
        }
        return begin;
    }
}
