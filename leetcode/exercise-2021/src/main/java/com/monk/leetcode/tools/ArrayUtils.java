package com.monk.leetcode.tools;

import java.util.Random;

/**
 * 数组帮助类
 */
public class ArrayUtils {

    /**
     * 构建指定长度的随机数组(100以内)并打印出来
     *
     * @param length 数组长度
     */
    public static Integer[] buildRandomArray2(int length) {
        Integer[] arr = new Integer[length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new Random().nextInt(100);
        }
        return arr;
    }

    public static Integer[] copy(Integer[] array) {
        Integer[] arr = new Integer[array.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = array[i];
        }
        return arr;
    }

    /**
     * 构建指定长度的随机数组(100以内)并打印出来
     * @param length  数组长度
     */
    public static int[] buildRandomArray(int length) {
        int[] arr = new int[10];
        for(int i = 0; i < arr.length; i++){
            arr[i] = new Random().nextInt(100);
        }
        return arr;
    }

    /**
     * 打印数组
     * @param arr  数组
     */
    public static void printArr(int[] arr) {
        for(int i = 0; i < arr.length; i++){
            String str = ", ";
            if(i == arr.length - 1){
                str = "\r\n";
            }
            System.out.print(arr[i] + str);
        }
    }

    /**
     * 打印数组
     *
     * @param arr 数组
     */
    public static void printArr2(String str, Integer[] arr) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        for (int i = 0; i < arr.length; i++) {
            String split = ", ";
            if (i == arr.length - 1) {
                split = "";
            }
            sb.append(arr[i] + split);
        }
        System.out.println(sb.toString());
    }
}
