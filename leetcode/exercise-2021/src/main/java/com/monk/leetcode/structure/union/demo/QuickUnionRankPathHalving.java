package com.monk.leetcode.structure.union.demo;

/**
 * QuickUnion 基于rank(子节点高度)的优化 + 路径减半(Path Halving)
 */
public class QuickUnionRankPathHalving extends QuickUnionRank {


    public QuickUnionRankPathHalving(int capacity) {
        super(capacity);
    }

    /**
     * 路径压缩的思想是 在find的时候,将每一个节点的父节点指向祖父节点
     * @param value
     * @return
     */
    @Override
    public int find(int value) {
        rangeCheck(value);

        // 将每隔一个节点的父节点指向祖父节点
        while (value != parents[value]){
            parents[value] = parents[parents[value]];
            value = parents[value];
        }

        return value;
    }
}
