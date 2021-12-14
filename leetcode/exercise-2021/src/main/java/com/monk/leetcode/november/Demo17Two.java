package com.monk.leetcode.november;

import com.monk.leetcode.tools.ArrayUtils;

/**
 * 使用数组表示二叉树并实现排序
 * 实现思路:
 *  1. 建里最大堆
 *  2. 将最大的数与数组中最后一个数进行交换
 */
public class Demo17Two {

    public static void main(String[] args) {
        int[] arr = buildRandomArray(10);
        int end = arr.length;
        while (end > 2){
            // 1. 建立最大堆
            // 从下往上,从右往左遍历二叉树
            for(int i = (end-1)/2; i >= 1; i--){
                //找出最大孩子索引
                int maxIdx = i * 2;
                if(maxIdx + 1 < end && arr[maxIdx + 1] > arr[maxIdx]){
                    //如果存在右孩子节点且右孩子 > 左孩子,就将最大只的索引设置为右孩子节点的索引
                    maxIdx++;
                }
                if(arr[maxIdx] > arr[i]){
                    //如果最大值孩子节点大于父节点,就将两个值进行交换
                    int temp = arr[i];
                    arr[i] = arr[maxIdx];
                    arr[maxIdx] = temp;
                }
            }

            // 2. 将最大堆和最后一个数字交换
            int temp = arr[1];
            arr[1] = arr[end -1];
            arr[end-1] = temp;
            end--;
        }

        ArrayUtils.printArr(arr);
    }

    /**
     * 构建指定长度的随机数组(100以内)并打印出来
     * @param length  数组长度
     */
    private static int[] buildRandomArray(int length) {

        int[] arr = ArrayUtils.buildRandomArray(length);

        /**
         * 这里将第一个数组中的第一个值规定修改为0(可以为任意数),因为在
         * 二叉树中,数组的一个值往往都会被忽略掉
         */
        arr[0] = 0;
        ArrayUtils.printArr(arr);
        return arr;
    }

}
