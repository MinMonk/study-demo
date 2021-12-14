package com.monk.leetcode.sort;

/**
 * 冒泡排序(优化版1)
 */
public class BubbleSort1<E extends Comparable> extends Sort<E> {

    @Override
    protected void sort() {
        for (int end = array.length - 1; end > 0; end--) {
            // 记录下当前这次扫描的结果是否是有序的
            boolean sorted = true;
            for (int begin = 0; begin < end; begin++) {
                if (compare(begin, begin + 1) > 0) {
                    swap(begin, begin + 1);
                    sorted = false;
                }
            }
            // 如果当前数组已经是有序的了,就可以直接终止外层的循环,直接break;
            if (sorted) break;
        }
    }
}
