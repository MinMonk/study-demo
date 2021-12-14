package com.monk.leetcode.structure.union.demo;

/**
 * 并查集
 */
public abstract class AbstractUnionFind {

    protected int[] parents;

    public AbstractUnionFind(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("The capacity must be >= 1");
        }
        parents = new int[capacity];
        for (int i = 0; i < parents.length; i++) {
            parents[i] = i;
        }
    }


    public abstract int find(int value);

    /**
     * 合并两个集合
     *  可以v1合并到v2,也可以v2合并到v1
     *  在这个demo中,默认是从v1合并到v2
     * @param v1
     * @param v2
     */
    public abstract void union(int v1, int v2);

    public boolean isSame(int v1, int v2) {
        return find(v1) == find(v2);
    }

    protected void rangeCheck(int v) {
        if (v < 0 || v >= parents.length) {
            throw new IllegalArgumentException("v is index of bounds.");
        }
    }


}
