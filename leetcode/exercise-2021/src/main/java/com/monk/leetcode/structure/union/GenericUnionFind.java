package com.monk.leetcode.structure.union;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * UnionFind泛型的应用
 *
 * @param <V>
 */
public class GenericUnionFind<V> {

    private Map<V, Node<V>> nodes = new HashMap<>();

    public void makeSet(V value) {
        nodes.put(value, new Node<>(value));
    }

    private Node<V> findNode(V value) {
        Node<V> node = nodes.get(value);
        if (node == null) return null;

        // 将每隔一个节点的父节点指向祖父节点
        while (!Objects.equals(node.value, node.parent.value)) {
            node.parent = node.parent.parent;
            node = node.parent;
        }
        return node;
    }

    public V find(V value) {
        Node<V> node = findNode(value);
        return null == node ? null : node.value;
    }

    public void union(V v1, V v2) {
        Node<V> p1 = findNode(v1);
        Node<V> p2 = findNode(v2);
        if (p1 == null || p2 == null) return;
        if (Objects.equals(p1, p2)) return;

        if (p1.rank < p2.rank) {
            // 当p1节点的树高小余p2节点的树高时,将p1节点合并到p2节点
            p1.parent = p2;
        } else if (p1.rank > p2.rank) {
            // 反之将p2合并到p1节点
            p2.parent = p1;
        } else {
            // 当相等时,随便哪个合并到那个都可以,但是需要注意的是,要维护下合并之后节点的树高
            p1.parent = p2;
            p2.rank += 1;
        }
    }

    public boolean isSame(V v1, V v2) {
        return Objects.equals(find(v1), find(v2));
    }

    private static class Node<V> {
        int rank;
        V value;
        Node<V> parent = this;

        public Node(V value) {
            this.value = value;
        }

    }
}
