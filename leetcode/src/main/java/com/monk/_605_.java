package com.monk;

/**
 * https://leetcode-cn.com/problems/can-place-flowers/
 *
 * 605. 种花问题
 */
public class _605_ {

    public static void main(String[] args) {
        System.out.println(canPlaceFlowers(new int[]{0}, 1));
    }

    public static boolean canPlaceFlowers(int[] flowerbed, int n) {
        if (flowerbed == null) return false;

        int count = 0;
        for (int i = 0; i < flowerbed.length; i++) {
            // 如果当前已种植的数量大于n,就可以直接返回结果了,不需要继续种植了
            if(count >= n ) return true;

            // 计算当前已经种植的数量
            // if(flowerbed[i] == 1){
            //     count++;
            //     continue;
            // }
            if (check(flowerbed, i)) {
                flowerbed[i] = 1;
                count++;
            }
        }
        return count >= n;
    }

    /**
     * 检查index位置是否能种植花
     * @param flowerbed
     * @param index
     * @return
     */
    static boolean check(int[] flowerbed, int index) {
        if(flowerbed.length == 1) return flowerbed[index] == 0;
        if (index == 0) {
            // 当种植的是第一颗的时候,只需要检查第一颗和第二颗有没有种植即可
            return flowerbed[index] == 0 && flowerbed[index + 1] == 0;
        } else if (index == flowerbed.length - 1) {
            // 当种植的是最后一颗的时候,只需要检查倒数第一颗和倒数第二颗有没有种植即可
            return flowerbed[index] == 0 && flowerbed[index - 1] == 0;
        } else {
            // 需要检查左,中,右三个位置有没有种植
            return flowerbed[index - 1] == 0 && flowerbed[index] == 0 && flowerbed[index + 1] == 0;
        }
    }
}
