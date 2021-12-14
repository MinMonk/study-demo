package com.monk.leetcode.structure.union.demo;

/**
 * QuickUnion 基于size的优化
 */
public class QuickUnionSize extends QuickUnion{

    private int[] size;//用于存储根节点下元素的个数

    public QuickUnionSize(int capacity) {
        super(capacity);

        // 初始化size集合,默认每个根节点下的元素个数均为1
        size = new int[capacity];
        for (int i = 0; i < parents.length; i++) {
            size[i] = 1;
        }
    }

    /**
     * 当合并两个集合的时候,让size较小的合并到size较大上,优化默认的规则-->从v1合并到v2
     * @param v1
     * @param v2
     */
    @Override
    public void union(int v1, int v2) {
        int p1 = find(v1);
        int p2 = find(v2);
        if(p1 == p2) return;

        if(size[p1] < size[p2]){
            // 当p1节点的元素个数小余p2节点下的元素个数时,将p1节点合并到p2节点
            parents[p1] = p2;
        }else{
            // 反之将p2合并到p1节点
            parents[p2] = p1;
        }
    }
}
