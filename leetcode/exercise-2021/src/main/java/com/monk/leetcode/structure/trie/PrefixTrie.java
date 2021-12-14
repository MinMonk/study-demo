package com.monk.leetcode.structure.trie;


import java.util.HashMap;

/**
 * 前缀树
 */
public class PrefixTrie<V> {

    private int size;

    private Node<V> root;

    /**
     * 树中元素的数量
     *
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * 判断树是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 清空前缀树
     */
    public void clear() {
        size = 0;
        root = null;
    }

    /**
     * 根据key从前缀树中获取某个元素的值
     *
     * @param key
     * @return
     */
    public V get(String key) {
        Node<V> node = node(key);
        return node != null && node.end ? node.value : null;
    }

    /**
     * 判断树中是否存在key
     *
     * @param key
     * @return
     */
    public boolean contains(String key) {
        Node<V> node = node(key);
        return node != null && node.end;
    }

    /**
     * 往树中添加元素
     *
     * @param key
     * @param value
     * @return
     */
    public V add(String key, V value) {
        verifyKey(key);

        // 如果根节点为空,就先创建根节点
        if (root == null) {
            root = new Node(null);
        }

        // 根节点不为空
        int keyLength = key.length();
        Node<V> node = root;
        for (int i = 0; i < keyLength; i++) {
            char c = key.charAt(i);
            boolean childrenEmpty = node.children == null;
            Node<V> childrenNode = childrenEmpty ? null : node.children.get(c);
            if (childrenNode == null) {
                // 如果当前节点的children属性为空,那么就创建这个children,并初始化children的children属性
                childrenNode = new Node(node);
                childrenNode.c = c;
                node.children = childrenEmpty ? new HashMap<>() : node.children;
                node.children.put(c, childrenNode);
            }
            node = childrenNode;
        }

        // 如果添加的这个元素已经存在,那么就覆盖并返回之前的值
        if (node.end) {
            V oldValue = node.value;
            node.value = value;
            return oldValue;
        }

        // 新增一个单词
        node.end = true;
        node.value = value;
        size++;

        return null;
    }

    /**
     * 根据key从树中删除元素
     *
     * @param key
     * @return
     */
    public V remove(String key) {
        Node<V> node = node(key);
        V oldValue = node.value;
        // 1. 先根据key判断这个元素是否存在,如果存在or是一个单词的尾部元素才删除,不存在直接返回
        if (node == null || !node.end) return null;
        size--;

        // 如果还存在子元素,那么只需要将end的标志位修改下即可,不需要删除这个节点
        if (node.children != null && !node.children.isEmpty()) {
            node.end = false;
            node.value = null;
            return oldValue;
        }

        /**
         * 如果不存在子元素就从当前元素开始向父节点删除
         * 如果父节点还有子元素 or 父节点是结束的标识(end=true)就不删除了
         */
        Node<V> parent = null;
        while ((parent = node.parent) != null) {
            parent.children.remove(node.c);
            if(parent.end || !parent.children.isEmpty()) break;
            node = parent;
        }
        return oldValue;
    }

    /**
     * 判断树中是否存在prefix开头的元素
     *
     * @param prefix
     * @return
     */
    public boolean startsWith(String prefix) {
        verifyKey(prefix);

        int keyLength = prefix.length();
        Node<V> node = root;
        for (int i = 0; i < keyLength; i++) {
            char c = prefix.charAt(i);
            node = node.children.get(c);
            // 如果节点为空或者节点没有孩子节点,说明树中不存在这样的元素,就直接返回空
            if (node == null || node.children.isEmpty()) return false;
        }

        return true;
    }

    /**
     * 根据key从树中找到对应的元素
     *
     * @param key
     * @return
     */
    private Node<V> node(String key) {
        verifyKey(key);

        int keyLength = key.length();
        Node<V> node = root;
        for (int i = 0; i < keyLength; i++) {
            // 如果节点为空或者节点没有孩子节点,说明树中不存在这样的元素,就直接返回空
            if (node == null || node.children.isEmpty()) return null;
            char c = key.charAt(i);
            node = node.children.get(c);
        }
        return node;
    }

    /**
     * 校验key是否合法(这里不允许为空)
     *
     * @param key
     */
    private void verifyKey(String key) {
        if (key == null || key.length() <= 0) {
            throw new IllegalArgumentException("The key must not be empty");
        }
    }

    /**
     * 前缀树内部类节点
     *
     * @param <V>
     */
    private static class Node<V> {
        Node parent;
        Character c;
        V value;
        boolean end;
        HashMap<Character, Node<V>> children;

        public Node(Node<V> parent) {
            this.parent = parent;
        }
    }
}
