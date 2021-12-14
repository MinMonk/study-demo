package com.monk.leetcode.structure.hash;

public interface Map<K, V> {

    int size();

    void clear();

    boolean isEmpty();

    V put(K key, V value);

    V get(K key);

    V remove(K key);

    boolean containsKey(K kkey);

    boolean containsValue(V value);

    void traversal(Visitor<K, V> visitor);

    public static abstract class Visitor<K, V> {
        boolean stop;
        public abstract boolean visit(K key, V value);
    }

}
