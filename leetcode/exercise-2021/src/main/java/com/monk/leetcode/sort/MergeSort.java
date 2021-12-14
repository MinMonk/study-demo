package com.monk.leetcode.sort;

/**
 * 归并排序
 *
 * @param <E>
 */
public class MergeSort<E extends Comparable> extends Sort<E> {

    private E[] leftArray;

    @Override
    protected void sort() {
        leftArray = (E[]) new Comparable[array.length >> 1];
        sort(0, array.length);
    }

    private void sort(int begin, int end) {
        // 当数组中的元素不足两个时,就没有必要进行切割了,直接返回即可
        if (end - begin < 2) return;

        // 确定切割位置
        int mid = (begin + end) >> 1;
        sort(begin, mid);
        sort(mid, end);
        merge(begin, mid, end);
    }

    private void merge(int begin, int mid, int end) {
        int li = 0, le = mid - begin;//记录左边数组的开始/结束位置
        int ri = mid, re = end;//记录右边数组的开始/结束位置
        int ai = begin;//记录被覆盖的元素位置

        // 将原数组的左边部分拷贝到提前准备的leftArray中
        for (int i = li; i < le; i++) {
            leftArray[i] = array[begin + i];
        }

        while (li < le) {
            if (ri < re && compare(array[ri], leftArray[li]) < 0) {
                // 如果右边数组的元素 < 左边数组的元素,就将右边数组的元素覆盖到ai位置,之后ri和ai指针都往右移动
                array[ai++] = array[ri++];
            } else {
                // 如果右边数组的元素 >= 左边数组的元素,那么就将左边数组(备份数组)的元素覆盖到ai位置,之后ri和li指针都往右移动
                array[ai++] = leftArray[li++];
            }
        }
    }
}
