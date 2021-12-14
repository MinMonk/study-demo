package com.monk.leetcode.november;

import com.monk.leetcode.tools.ArrayUtils;

/**
 * 快速排序
 */
public class Demo16 {

    public static void main(String[] args) {
        int[] arr = ArrayUtils.buildRandomArray(10);
        ArrayUtils.printArr(arr);

        sort(arr,0,arr.length-1);
        ArrayUtils.printArr(arr);
    }

    public static void sort(int[] arr, int s, int e){
        if(s < e){
            int m = sortUnit(arr,s,e);//找出中间值
            sort(arr,s,m-1);//对左边进行排序
            sort(arr,m+1, e);//对右边进行排序
        }
    }

    public static int sortUnit(int[] arr, int s, int e){
        int num = arr[s];//记录标杆值
        int i= s;//记录从左往右循环的位置
        int j= e;//记录从右往左循环的位置

        while (i < j){
            while (i < j){
                // 从右往左循环时,将大于标杆值的数字放到左边的循环里
                if(arr[j] < num){
                    arr[i] = arr[j];
                    break;
                }
                j--;
            }

            while (i < j){
                // 从左往右循环时,将<=标杆值的数字放到右边的循环里
                if(arr[i] >= num){
                    arr[j] = arr[i];
                    break;
                }
                i++;
            }
        }
        arr[i] = num;
        return i;
    }
}
