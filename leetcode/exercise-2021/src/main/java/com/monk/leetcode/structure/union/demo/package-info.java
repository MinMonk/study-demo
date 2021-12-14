package com.monk.leetcode.structure.union.demo;

/**
 * 当前包中的类都是并查集的demo类
 * 父类为AbstractUnionFind
 * 一级子类  QuickFind + QuickUnion
 * 二级子类(均为QuickUnion的子类)  QuickUnionSize + QuickUnionRank
 * 三级子类(均为QuickUnionRank的子类) QuickUnionRankPathCompressing + QuickUnionRankPathSpliting + QuickUnionRankPathHalving
 *
 * 推荐使用QuickUnionRankPathSpliting + QuickUnionRankPathHalving
 * 是最优的优化后的结果
 */