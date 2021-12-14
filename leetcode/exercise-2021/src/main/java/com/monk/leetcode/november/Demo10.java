package com.monk.leetcode.november;

/**
 * 二分查找法
 */
public class Demo10 {

    public static void main(String[] args) {
        int[] arr = {2,4,6,7,8,9,13,15,24,46,57};
        int num = 14;

        int s = 0;  //开始位置
        int e = arr.length - 1;  //结束位置
        int m = 0;  //中间位置
        while(s <= e){
            m = (s + e) / 2;
            if(num == arr[m]){
                System.out.println("找到了,索引为:" + m);
                return;//使用return而不用break,是为了避免找到了,还会输出一句"不存在"
            }else if(num < arr[m]){
                // 如果要查找的值小余中间值,那么就移动结束位置的指针为中间值的前一个
                e = m -1;
            }else{
                // 如果要查找的值大于中间值,那么就移动开始位置的指针为中间值的后一个
                s = m + 1;
            }
        }
        System.out.println("不存在");
    }
}
