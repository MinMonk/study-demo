package com.monk.leetcode.november;

import com.monk.leetcode.tools.ArrayUtils;

/**
 * 冒泡排序 + 插入排序
 */
public class Demo12 {

    public static void main(String args[]) {

        // int[] arr = ArrayUtils.buildRandomArray(10);
        // ArrayUtils.printArr(arr);

        // bubbleSort(arr);
        // bubbleSort2(arr);
        // insertSort(arr);
        // selectionSort(arr);
    }


    /**
     * 选择排序
     *
     * @param arr
     */
    private static void selectionSort(int[] arr) {
        for (int length = arr.length - 1; length > 0; length--) {
            int maxIdx = 0;
            for (int i = 0; i <= length; i++) {
                if (arr[maxIdx] < arr[i]) {
                    maxIdx = i;
                }
            }
            int temp = arr[length];
            arr[length] = arr[maxIdx];
            arr[maxIdx] = temp;
        }
        ArrayUtils.printArr(arr);
    }

    /**
     * 冒泡排序原始版
     *
     * @param arr
     */
    private static void bubbleSort0(int[] arr) {
        for (int end = arr.length - 1; end > 0; end--) {
            for (int begin = 0; begin < end; begin++) {
                if (arr[begin] > arr[begin + 1]) {
                    int temp = arr[begin + 1];
                    arr[begin + 1] = arr[begin];
                    arr[begin] = temp;
                }
            }
        }

        ArrayUtils.printArr(arr);
    }

    /**
     * 冒泡排序优化版1
     *
     * @param arr
     */
    private static void bubbleSort1(int[] arr) {
        for (int end = arr.length - 1; end > 0; end--) {
            // 记录下当前这次扫描的结果是否是有序的
            boolean sorted = true;
            for (int begin = 0; begin < end; begin++) {
                if (arr[begin] > arr[begin + 1]) {
                    int temp = arr[begin + 1];
                    arr[begin + 1] = arr[begin];
                    arr[begin] = temp;
                    sorted = false;
                }
            }
            // 如果当前数组已经是有序的了,就可以直接终止外层的循环,直接break;
            if (sorted) break;
            ;
        }

        ArrayUtils.printArr(arr);
    }

    /**
     * 冒泡排序优化版2
     *
     * @param arr
     */
    private static void bubbleSort2(int[] arr) {
        for (int end = arr.length - 1; end > 0; end--) {
            /**
             * 用于存储最后一次交换顺序的索引,如果不存在交换,那么就说明这个数组就是一个有序的数组
             * 如果只是尾部有顺序,中途是无序的话,那么就记录下最后一次交换的位置,然后下一次循环的时候,
             * 只需要遍历到上一次交换的位置即可
             */
            int sortedIdx = 0;
            for (int begin = 0; begin < end; begin++) {
                if (arr[begin] > arr[begin + 1]) {
                    int temp = arr[begin + 1];
                    arr[begin + 1] = arr[begin];
                    arr[begin] = temp;
                    sortedIdx = begin + 1;
                }
            }
            // 将最后一次交换位置的索引赋值给end,减少第2层for循环遍历的次数
            end = sortedIdx;
        }

        ArrayUtils.printArr(arr);
    }


    /**
     * 插入排序
     *
     * @param arr 要排序的数组
     */
    private static void insertSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i - 1]) {
                for (int j = 0; j < i; j++) {
                    if (arr[i] < arr[j]) {
                        // 如果arr[i] 小余 arr[j],则需要将这个值插入到j前面
                        int temp = arr[i];
                        for (int k = i - 1; k >= j; k--) {
                            arr[k + 1] = arr[k];
                        }
                        arr[j] = temp;
                        break;
                    }
                }
            }
        }

        ArrayUtils.printArr(arr);
    }

    /**
     * 冒泡排序
     *
     * @param arr 要排序的数组
     */
    private static void bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] < arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        ArrayUtils.printArr(arr);
    }

    /**
     * 不用第三个数完成两个数值的交换
     */
    private static void swapValue() {
        int a = 5;
        int b = 3;
        System.out.println(a + "," + b);
        a = a ^ b;
        b = a ^ b;
        a = a ^ b;
        System.out.println(a + "," + b);
    }
}
