package com.monk.leetcode.sort;

import java.beans.BeanInfo;

/**
 * 快速排序
 *
 * @param <E>
 */
public class QuickSort<E extends Comparable> extends Sort<E> {

    @Override
    protected void sort() {
        sort(0, array.length);
    }

    private void sort(int begin, int end){
        // 当数组中的元素不足两个时,就没有必要进行切割了,直接返回即可
        if (end - begin < 2) return;

        // 确定切割位置
        int pivotIndex = pivotIndex(begin, end);

        // 对切割后的数组进行排序
        sort(begin, pivotIndex);
        sort(pivotIndex + 1, end);
    }

    /**
     * 构建[begin, end)这个数组区间的轴点,并返回轴点所在的索引位置
     * @return
     */
    private int pivotIndex(int begin, int end) {
        E pivot = array[begin];

        // 由于数组是左闭右开原则,所以这里需要end--才能表示是最后一个元素,否则会数组越界
        end--;
        while (begin < end) {
            // 先从end --> begin方向扫描
            while (begin < end) {
                /**
                 * 注意:
                 *  这里比较时,等于也交换位置,是为了以轴点切割数组的时候,让元素的分布更加均匀,避免出现最早的情况
                 */
                if (compare(pivot, array[end]) < 0) {
                    // 如果end位置的元素 < 轴点位置的元素
                    end--;
                } else {
                    /**
                     * 如果end位置的元素 >= 轴点位置的元素,那么就将end位置的元素覆盖到begin位置
                     * 并且覆盖完后,需要对begin进行右移
                     */
                    array[begin++] = array[end];
                    break;
                }
            }

            while (begin < end) {
                if (compare(pivot, array[begin]) > 0) {
                    // 如果begin位置的元素 < 轴点位置的元素
                    begin++;
                } else {
                    /**
                     * 如果begin位置的元素 >= 轴点位置的元素,那么就将begin位置的元素覆盖到原end位置
                     * 并且覆盖完后,需要对end进行左移
                     */
                    array[end--] = array[begin];
                    break;
                }
            }
        }

        array[begin] = pivot;
        return begin;
    }
}
