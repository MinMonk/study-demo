package com.monk.leetcode.structure.union;

import com.monk.leetcode.structure.union.demo.*;
import com.monk.leetcode.tools.Asserts;
import com.monk.leetcode.tools.Times;

public class GenericUnionFindTest {

    private final static int count = 1000000;

    public static void main(String[] args) {
        testTimes(new QuickFind(count));
        testTimes(new QuickUnion(count));
        testTimes(new QuickUnionSize(count));
        testTimes(new QuickUnionRank(count));
        testTimes(new QuickUnionRankPathCompressing(count));
        testTimes(new QuickUnionRankPathSpliting(count));
        testTimes(new QuickUnionRankPathHalving(count));

        testGenericUnionFindTimeComplete();
        testGenericUnionFindTime(new GenericUnionFind<>());
    }

    /**
     * 测试GenericUnionFindTime功能是否完整
     */
    private static void testGenericUnionFindTimeComplete() {
        GenericUnionFind<Student> uf = new GenericUnionFind<Student>();
        Student s1 = new Student(1,"jack");
        Student s2 = new Student(2,"rose");
        Student s3 = new Student(3,"jim");
        Student s4 = new Student(4,"tom");
        uf.makeSet(s1);
        uf.makeSet(s2);
        uf.makeSet(s3);
        uf.makeSet(s4);

        uf.union(s1, s2);
        uf.union(s3, s4);
        Asserts.test(uf.isSame(s1,s2));
        Asserts.test(!uf.isSame(s2,s4));
        uf.union(s1,s3);
        Asserts.test(uf.isSame(s2,s4));
    }

    private static void testGenericUnionFindTime(GenericUnionFind<Integer> uf) {
        for (int i = 0; i < count; i++) {
            uf.makeSet(i);
        }
        Times.test(uf.getClass().getSimpleName(), () -> {
            for (int i = 0; i < count; i++) {
                uf.union((int) Math.random() * count, (int) Math.random() * count);
            }

            for (int i = 0; i < count; i++) {
                uf.isSame((int) Math.random() * count, (int) Math.random() * count);
            }
        });
    }

    private static void testTimes(AbstractUnionFind uf) {
        Times.test(uf.getClass().getSimpleName(), () -> {
            for (int i = 0; i < count; i++) {
                uf.union((int) Math.random() * count, (int) Math.random() * count);
            }

            for (int i = 0; i < count; i++) {
                uf.isSame((int) Math.random() * count, (int) Math.random() * count);
            }
        });
    }

    /**
     * 测试使用的内部Student类
     */
    private static class Student {
        int age;
        String name;

        public Student(int age, String name) {
            this.age = age;
            this.name = name;
        }
    }
}
