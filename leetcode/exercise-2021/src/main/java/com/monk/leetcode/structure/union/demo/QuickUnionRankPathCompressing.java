package com.monk.leetcode.structure.union.demo;

/**
 * QuickUnion 基于rank(子节点高度)的优化 + 路径压缩(PathCompressin)
 */
public class QuickUnionRankPathCompressing extends QuickUnionRank {


    public QuickUnionRankPathCompressing(int capacity) {
        super(capacity);
    }

    /**
     * 路径压缩的思想是 在find的时候,将每一个节点的父节点都指向根节点
     * @param value
     * @return
     */
    @Override
    public int find(int value) {
        rangeCheck(value);

        // 使用递归,在每一次找父节点的同时,将节点指向根节点
        if(value != parents[value]){
            parents[value] = find(parents[value]);
        }

        return parents[value];
    }
}
