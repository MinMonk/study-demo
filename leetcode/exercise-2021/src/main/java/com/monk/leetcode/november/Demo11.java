package com.monk.leetcode.november;

/**
 * 斐波拉契数列
 * 1 1 2 3 5 8 13 21 34
 */
public class Demo11 {

    public static void main(String[] args) {
        System.out.println(fib1(12));
        System.out.println(fib2(12));
        System.out.println(fib3(12));
        System.out.println(fib4(12));
    }

    /**
     * 递归方法实现
     *
     * 时间复杂度为 O(2^n)
     * @param n
     * @return
     */
    private static int fib1(int n){
        if(n <= 2) return 1;

        return fib1(n - 1) + fib1(n - 2);
    }

    /**
     * 使用for循环来实现
     * 空间复杂度为 O(n)
     *
     * @param n
     * @return
     */
    private static int fib2(int n){
        if(n <= 1) return 1;

        int first = 0;
        int second = 1;
        for (int i = 0; i < n - 1; i++) {
            int sum = first + second;
            first = second;
            second = sum;
        }
        return second;
    }

    /**
     * 使用while循环来实现(和for循环的区别是,进一步精简代码)
     * 空间复杂度为 O(n)
     *
     * @param n
     * @return
     */
    private static int fib3(int n){
        if(n <= 1) return 1;

        int first = 0;
        int second = 1;
        while(n-- > 1) {
            second += first;
            first = second - first;
        }
        return second;
    }

    /**
     * 可以利用他的线性代数解法公式
     * 具体公式可以百度,这个公式在这里无法通过打字的方式打出来
     * @param n
     * @return
     */
    private static int fib4(int n){
        //先求出根号5的值
        double a = Math.sqrt(5);

        // 再根据数学公式计算
        return (int)((Math.pow((1 + a)/2, n) - Math.pow((1 - a)/2, n))/a);
    }
}
