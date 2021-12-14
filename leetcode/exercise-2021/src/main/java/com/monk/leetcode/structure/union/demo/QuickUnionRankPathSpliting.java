package com.monk.leetcode.structure.union.demo;

/**
 * QuickUnion 基于rank(子节点高度)的优化 + 路径分裂(Path Spliting)
 */
public class QuickUnionRankPathSpliting extends QuickUnionRank {


    public QuickUnionRankPathSpliting(int capacity) {
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

        // 将每一个节点的父节点指向祖父节点
        while (value != parents[value]){
            int parent = parents[value];
            parents[value] = parents[parents[value]];
            value = parent;
        }

        return value;
    }
}
