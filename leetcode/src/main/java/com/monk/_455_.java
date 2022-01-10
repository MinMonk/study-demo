package com.monk;

import java.util.Arrays;

/**
 * https://leetcode-cn.com/problems/assign-cookies/
 * <p>
 * 455. 分发饼干
 */
public class _455_ {

    public static void main(String[] args) {
        System.out.println(findContentChildren(new int[]{1, 2, 3}, new int[]{1, 1}));
        System.out.println(findContentChildren(new int[]{1, 5, 3, 8}, new int[]{3, 2, 10}));
    }

    static int findContentChildren(int[] g, int[] s) {
        // 先根据孩子的胃口,饼干的尺寸进行从小到大排序,优先满足胃口小的孩子
        Arrays.sort(g);
        Arrays.sort(s);
        int i = 0, j = 0;
        while (i < g.length && j < s.length) {
            if (g[i] <= s[j]) {
                // 优先满足胃口小的孩子,满足一个孩子,孩子的指针后移一位,尝试第二个孩子
                i++;
            }
            // 循环一次,饼干的指针后移一位
            j++;
        }
        return i;
    }
}
