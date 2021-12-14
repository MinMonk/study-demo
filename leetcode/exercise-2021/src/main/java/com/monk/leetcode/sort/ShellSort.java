package com.monk.leetcode.sort;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 希尔排序
 *
 *  也可以视为是对插入排序的一种优化,通过将数组转换为矩阵的方式来减少**逆序对**的数量,从而提高插入排序算法的性能
 *
 *  本demo中提供了两种构建步长序列的方式:
 *      1. 由希尔本人提供的,最坏的情况时间复杂度是 O(n ^ 2);
 *      2. 由Robert Sedgewick提供的,最坏的情况时间复杂度是 O(n ^ 3/4)
 */
public class ShellSort<E extends Comparable> extends Sort<E> {
    @Override
    protected void sort() {
        List<Integer> stepSequence = shellStepSequence();
        for (Integer step : stepSequence) {
            sort(step);
        }
    }

    private void sort(int step) {
        for (int col = 0; col < step; col++) {
            for (int begin = col + step; begin < array.length; begin += step) {
                int curr = begin;
                E val = array[begin];
                while (curr > col && compare(array[curr - step], val) > 0) {
                    array[curr] = array[curr - step];
                    curr -= step;
                }
                array[curr] = val;
            }
        }
    }

    /**
     * 希尔本人提供的步长序列  最坏的情况是 O(n ^ 2)
     * @return
     */
    private List<Integer> shellStepSequence() {
        List<Integer> stepSequence = new ArrayList<>();
        int step = array.length;
        while ((step >>= 1) > 0) {
            stepSequence.add(step);
        }
        return stepSequence;
    }

    /**
     * 由Robert Sedgewick提出的步长序列  最坏的情况是 O(n ^ 3/4)
     * @return
     */
    private List<Integer> sedgewickStepSequence() {
        List<Integer> stepSequence = new LinkedList<>();
        int k = 0, step = 0;
        while (true) {
            if (k % 2 == 0) {
                int pow = (int) Math.pow(2, k >> 1);
                step = 1 + 9 * (pow * pow - pow);
            } else {
                int pow1 = (int) Math.pow(2, (k - 1) >> 1);
                int pow2 = (int) Math.pow(2, (k + 1) >> 1);
                step = 1 + 8 * pow1 * pow2 - 6 * pow2;
            }
            if (step >= array.length) break;
            stepSequence.add(0, step);
            k++;
        }
        return stepSequence;
    }
}
