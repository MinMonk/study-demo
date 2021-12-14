package com.monk.leetcode.structure.tree;

import java.util.Comparator;

/**
 * 二叉搜索树
 * @param <E>
 */
public class BSTree<E> extends BinaryTree<E>{

    private Comparator<E> comparator;

    public BSTree() {

    }

    public BSTree(Comparator<E> comparator) {
        this.comparator = comparator;
    }
    /**
     * 添加元素
     * @param element
     */
    public void add(E element) {
        if (null == root) {
            root = createNode(element, null);
            size++;

            afterAdd(root);
        } else {
            Node<E> node = root;
            Node<E> parent = root;
            int res = 0;
            while (node != null) {
                parent = node;
                res = compare(element, node.element);
                if (res > 0) {
                    node = node.right;
                } else if (res < 0) {
                    node = node.left;
                } else {
                    // 当相等时
                    node.element = element;
                    return;
                }
            }
            Node<E> newNode = createNode(element, parent);
            if (res > 0) {
                parent.right = newNode;
            } else {
                parent.left = newNode;
            }
            size++;

            afterAdd(newNode);
        }
    }

    /**
     * 添加节点后的操作,交给子类去扩展
     * @param node
     */
    protected void afterAdd(Node<E> node) {

    }

    /**
     * 删除节点后的操作,交给子类去扩展
     * @param node  要删除的节点
     */
    protected void afterRemove(Node<E> node) {

    }

    /**
     * 创建一个节点
     * @param element  节点值
     * @param parent  父节点
     * @return
     */
    protected Node<E> createNode(E element, Node<E> parent){
        return new Node<>(element, parent);
    }

    /**
     * 比较两个节点的大小
     * @param e1
     * @param e2
     * @return
     */
    private int compare(E e1, E e2) {
        if (null != comparator) {
            return comparator.compare(e1, e2);
        }

        return ((Comparable) e1).compareTo(e2);
    }

    /**
     * 删除指定元素
     * @param element
     */
    public void remove(E element) {
        if(element == null) return;;

        remove(node(element));
    }

    /**
     * 删除指定节点
     * @param node
     */
    private void remove(Node<E> node) {
        if(node == null) return;

        /**
         * 1. 先删除度为2的节点
         */
        if(node.hasTwoLeaf()){
            // 1.1. 先找到当前节点的前驱or后继节点(这里自己定义,这里姑且定义为找到后继节点,因为大多数情况下都是找后继节点)
            Node<E> next = successor(node);

            // 1.2. 用后继节点的值覆盖原节点的值
            node.element = next.element;

            /**
             * <pre>
             * 1.3. 删除后继节点
             *      分析:
             *          这里有点类似于降维打击的意思,因为当前方法是删除一个节点,只是先删除度为2的节点,那么这里将度为2的节点降维成<2的节点,
             *          那么在当前方法中,当前行代码的后续会处理这种度<2的节点,交给后续的代码去删除这个后继节点
             * </pre>
             */
            node = next;
        }

        /**
         * 2. 处理度为1的节点
         */
        // 2.1. 先获取要删除节点的子节点
        Node<E> replacement = node.left != null ? node.left : node.right;
        // 2.2. 将子节点的parent指针指向该节点的父节点
        if(replacement != null){
            replacement.parent = node.parent;
            // 2.3. 完成子节点的替换
            if(node.parent == null){
                // 如果要删除的节点度为1,切父节点为空,那么这个节点只可能是root节点,那么将root节点指向该节点的孩子节点(replacement)即可
                root = replacement;
            }else if(node.isLeftChild()){
                // 如果要删除节点是出于父节点的左边,那就将父节点的左指针指向该节点的孩子节点(replacement)即可
                node.parent.left = replacement;
            }else{
                // 如果要删除节点是出于父节点的右边,那就将父节点的右指针指向该节点的孩子节点(replacement)即可
                node.parent.right = replacement;
            }

            afterRemove(replacement);
        }else if(node.parent == null){
            /**
             * <pre>
             * 3. 处理度为0的节点
             *      分析:
             *          1. 判断该节点是否是根节点
             *          2. 判断该叶子节点是处于父节点的左侧还是右侧
             *          3. 将父节点的左/右指针置空
             *
             * </pre>
             */
            // 3.1 如果要删除节点的父节点为空,那么该节点必然为root节点,而删除root节点,只需要要root置空即可
            root = null;

            afterRemove(node);
        }else{
            // 3.2 判断该叶子节点是处于父节点的左侧还是右侧
            if(node.isLeftChild()){
                // 3.3 将父节点的左指针置空
                node.parent.left = null;
            }else{
                // 3.3 将父节点的右指针置空
                node.parent.right = null;
            }

            afterRemove(node);
        }

        size--;
    }

    /**
     * 根据元素找到对应的节点
     *
     * @param element
     * @return
     */
    private Node<E> node(E element) {
        if (element == null) return null;

        Node<E> node = root;
        while (node != null){
            int compare = compare(element, node.element);

            // 如果相等则说明找到了,就直接返回
            if(compare == 0) return node;

            if(compare > 0){
                node = node.right;
            }else{
                node = node.left;
            }
        }

        return null;
    }

    /**
     * 判断元素是否存在
     *
     * @param element
     * @return
     */
    public boolean contains(E element) {
        return node(element) != null;
    }

    public Node<E> getRoot() {
        return root;
    }

}
