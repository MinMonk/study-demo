package com.monk.leetcode.april;

import java.util.HashMap;

/**
 * 2020-04-29 Leetcode练习题
 * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
 * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素不能使用两遍。
 * 示例:
 *      给定 nums = [2, 7, 11, 15], target = 9
 *      因为 nums[0] + nums[1] = 2 + 7 = 9
 *      所以返回 [0, 1]
 *
 * @author Monk
 */
public class TwentyNine {

    public static void main(String[] args) {
        int[] nums = new int[]{3, 2, 60, 8, 10, 30, 5, 7, 15};
        int[] result = twoSum(nums, 9);
        for(int temp : result){
            System.out.println(temp);
        }
        System.out.println("-------------第二种算法----------------");
        nums = new int[]{2, 5, 9, 5, 10};
        result = twoSum2(nums, 10);
        for(int temp : result){
            System.out.println(temp);
        }
    }

    /*
     * 功能描述: <br>
     * 〈找出数组中和为目标值的那两个整数，并返回他们的数组下标〉<br>
     * 自己写的暴力算法
     * @Param: [nums：数组， target：目标值]
     * @Return: int[]  返回和为目标值得两个整数的下标
     * @Author: Monk
     * @Date: 2020/4/29 14:53
     */
    public static int[] twoSum(int[] nums, int target) {
        int[] result = new int[2];
        for(int i = 0; i < nums.length; i++){
            for(int j = nums.length - 1; j > 0; j--){
                // 避免一个数重复加两次
                if(i != j){
                    if(target == nums[i] + nums[j]){
                        result[0] = i;
                        result[1] = j;
                        return result;
                    }
                }
            }
        }

        return result;
    }

    /*
     * 功能描述: <br>
     * 〈找出数组中和为目标值的那两个整数，并返回他们的数组下标〉<br>
     * LeetCode评论中看到的高效率写法
     * @Param: [nums：数组， target：目标值]
     * @Return: int[]  返回和为目标值得两个整数的下标
     * @Author: Monk
     * @Date: 2020/4/29 14:53
     */
    public static int[] twoSum2(int[] nums, int target) {
        int[] result = new int[2];
        HashMap<Integer,Integer> hash = new HashMap<Integer,Integer>();
        for(int i = 0; i < nums.length; i++){
            if(hash.containsKey(nums[i])){
                result[0] = i;
                result[1] = hash.get(nums[i]);
                return result;
            }
            // 将数据存入 key为补数 ，value为下标
            hash.put(target-nums[i], i);
        }

        return result;
    }
}
