package com.monk.leetcode.structure.union.demo;

/**
 * QuickFind
 */
public class QuickFind extends AbstractUnionFind{

    public QuickFind(int capacity) {
        super(capacity);
    }

    @Override
    public int find(int value) {
        rangeCheck(value);
        return parents[value];
    }

    @Override
    public void union(int v1, int v2) {
        int p1 = find(v1);
        int p2 = find(v2);
        if(p1 == p2) return;

        // 将父节点为p1的全部改为p2
        for (int i = 0; i < parents.length; i++) {
            if(parents[i] == p1){
                parents[i] = p2;
            }
        }

    }
}
