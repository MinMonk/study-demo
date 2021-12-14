package com.monk.leetcode.structure.union.demo;

/**
 * QuickUnion
 */
public class QuickUnion extends AbstractUnionFind {

    public QuickUnion(int capacity) {
        super(capacity);
    }

    @Override
    public int find(int value) {
        rangeCheck(value);

        while (value != parents[value]){
            value = parents[value];
        }

        return value;
    }

    @Override
    public void union(int v1, int v2) {
        int p1 = find(v1);
        int p2 = find(v2);
        if(p1 == p2) return;

        // 将v1的父节点改为v2的父节点
        parents[p1] = p2;
    }
}