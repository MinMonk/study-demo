package com.monk.leetcode.structure.union.demo;

/**
 * QuickUnion 基于rank(子节点高度)的优化
 */
public class QuickUnionRank extends QuickUnion {

    private int[] ranks;

    public QuickUnionRank(int capacity) {
        super(capacity);

        // 初始化ranks集合,默认每个根节点下的树高均为1
        ranks = new int[capacity];
        for (int i = 0; i < parents.length; i++) {
            ranks[i] = 1;
        }
    }

    /**
     * 当合并两个集合的时候,让rank较小的合并到rank较大上,优化默认的规则-->从v1合并到v2
     * 其实这里有rank的概念是因为,实则每一个根节点就是一个多叉书,所以这个优化点就是让树高低的合并到树高高的节点上
     * 从而检查find时的时间复杂度,让树相对更平衡
     *
     * @param v1
     * @param v2
     */
    @Override
    public void union(int v1, int v2) {
        int p1 = find(v1);
        int p2 = find(v2);
        if (p1 == p2) return;

        if (ranks[p1] < ranks[p2]) {
            // 当p1节点的树高小余p2节点的树高时,将p1节点合并到p2节点
            parents[p1] = p2;
        } else if (ranks[p1] > ranks[p2]) {
            // 反之将p2合并到p1节点
            parents[p2] = p1;
        } else {
            // 当相等时,随便哪个合并到那个都可以,但是需要注意的是,要维护下合并之后节点的树高
            parents[p1] = p2;
            ranks[p2] += 1;
        }
    }
}
