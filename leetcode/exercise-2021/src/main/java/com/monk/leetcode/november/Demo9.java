package com.monk.leetcode.november;


import com.monk.leetcode.tools.ArrayUtils;

public class Demo9 {

    public static void main(String[] args) {
        // 5!
        int a = 1;
        int n = 5;
        for(int i = 1; i <= n; i++){
            a *= i;
        }

        System.out.println(a);

        // 30的阶乘
        int[] a1 = new int[40];
        a1[a1.length-1] = 1;
        int n1 = 30;
        for(int i = 1; i <= n1; i++){
            a1 = arrayMuliNum(a1,i);
        }
        ArrayUtils.printArr(a1);


        int a2 = 7685;
        int b2 = 56;
        ArrayUtils.printArr(intToArr(a2, 10));
        ArrayUtils.printArr(arrayMuliNum(intToArr(a2, 100), b2));
    }

    /**
     * 将int数字转成int类型的数组
     * @param num  int数字
     * @param len  转换后数组的长度
     * @return   转换后的数组
     */
    private static int[] intToArr(int num, int len) {
        len = len >= 10 ? len : 10;
        int[] arr = new int[len];
        int idx = 1;
        String str = String.valueOf(num);
        for(int i = str.length()-1; i >=0; i--){
            arr[arr.length - idx] = Integer.valueOf(String.valueOf(str.charAt(i)));
            idx++;
        }
        return arr;
    }

    /**
     * 数组乘以数
     * @param arr  数组
     * @param num  数
     * @return 积
     */
    private static int[] arrayMuliNum(int[] arr, int num) {
        for (int i = 0; i < arr.length; i++){
            arr[i] *= num;
        }

        for (int i = arr.length; i > 1; i--){
            arr[i-2] = arr[i-2] + arr[i-1]/10;
            arr[i-1] = arr[i-1]%10;
        }
        return arr;
    }

    /**
     * 获取数组中的最高位索引
     * @param num  数组
     * @return  最高位的索引
     */
    static int getFirst(int[] num){
        for (int i = 0; i < num.length; i++){
            if(num[i] > 0){
                return i;
            }
        }
        return num.length;
    }
}
