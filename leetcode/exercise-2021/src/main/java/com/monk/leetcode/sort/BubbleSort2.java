package com.monk.leetcode.sort;

/**
 * 冒泡排序(优化版2)
 */
public class BubbleSort2<E extends Comparable> extends Sort<E> {

    @Override
    protected void sort() {
        for (int end = array.length - 1; end > 0; end--) {
            /**
             * 用于存储最后一次交换顺序的索引,如果不存在交换,那么就说明这个数组就是一个有序的数组
             * 如果只是尾部有顺序,中途是无序的话,那么就记录下最后一次交换的位置,然后下一次循环的时候,
             * 只需要遍历到上一次交换的位置即可
             */
            int sortedIdx = 0;
            for (int begin = 0; begin < end; begin++) {
                if (compare(begin, begin + 1) > 0) {
                    swap(begin, begin + 1);
                    sortedIdx = begin + 1;
                }
            }
            // 将最后一次交换位置的索引赋值给end,减少第2层for循环遍历的次数
            end = sortedIdx;
        }
    }
}
