package com.monk.leetcode.structure.union.demo;


import com.monk.leetcode.tools.Asserts;
import com.monk.leetcode.tools.Times;

public class TestUnionFind {

    private final static int count = 10000000;
    public static void main(String[] args) {

        testPerformance(
                new QuickUnion(count),
                new QuickUnionSize(count),
                new QuickUnionRank(count),
                new QuickUnionRankPathCompressing(count),
                new QuickUnionRankPathSpliting(count),
                new QuickUnionRankPathHalving(count)
                );
    }

    private static void testPerformance(AbstractUnionFind... ufs) {
        for(AbstractUnionFind uf : ufs){
            test(uf);
            Times.test(uf.getClass().getSimpleName(), () -> {
                for (int i = 0; i < count; i++) {
                    uf.union((int) Math.random() * count, (int) Math.random() * count);
                }

                for (int i = 0; i < count; i++) {
                    uf.isSame((int) Math.random() * count, (int) Math.random() * count);
                }
            });
        }
    }

    private static void test(AbstractUnionFind uf) {
        uf.union(0, 1);
        uf.union(0, 3);
        uf.union(0, 4);
        uf.union(2, 3);
        uf.union(2, 5);

        uf.union(6, 7);

        uf.union(8, 10);
        uf.union(9, 10);
        uf.union(9, 11);

        Asserts.test(uf.isSame(1, 5));
        Asserts.test(!uf.isSame(1, 7));
        Asserts.test(uf.isSame(8, 10));
        Asserts.test(!uf.isSame(1, 8));
        uf.union(1, 7);
        Asserts.test(uf.isSame(3, 6));
        uf.union(8, 7);
        Asserts.test(uf.isSame(2, 11));
    }
}
