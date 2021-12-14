package com.monk.leetcode.structure.tree;

import java.util.Comparator;

/**
 * 平衡树
 * @param <E>
 */
public class BalanceTree<E> extends BSTree<E>{

    public BalanceTree(){

    }

    public BalanceTree(Comparator<E> comparator) {
        super(comparator);
    }

    /**
     * 左旋转
     * <pre>
     *     需要做的事情:
     *      1. grand.right = parent.left
     *      2. parent.left = grand
     *      3. 让parent作为当前子树的根节点
     *      4. 更新节点(child,parent,grand)的parent属性
     *      5. 更新节点的高度height属性
     * </pre>
     * @param grand
     */
    protected void rotateLeft(Node<E> grand){
        Node<E> parent = grand.right;
        Node<E> child = parent.left;

        grand.right = child;
        parent.left = grand;

        afterRotate(grand,parent,child);
    }

    /**
     * 右旋转
     * <pre>
     *     需要做的事情:
     *      1. grand.left = parent.right
     *      2. parent.right = grand
     *      3. 让parent作为当前子树的根节点
     *      4. 更新节点(child,parent,grand)的parent属性
     *      5. 更新节点的高度height属性
     * </pre>
     * @param grand
     */
    protected void rotateRight(Node<E> grand){
        Node<E> parent = grand.left;
        Node<E> child = parent.right;

        grand.left = child;
        parent.right = grand;

        afterRotate(grand,parent,child);
    }

    /**
     * 旋转操作后的公共操作
     * <pre>
     *      3. 让parent作为当前子树的根节点
     *      4. 更新节点(child,parent,grand)的parent属性
     *      5. 更新节点的高度height属性
     * </pre>
     * @param grand
     * @param parent
     * @param child
     */
    protected void afterRotate(Node<E> grand, Node<E> parent, Node<E> child){
        // 1. 让parent作为当前子树的根节点
        parent.parent = grand.parent;
        if(grand.isLeftChild()){
            grand.parent.left = parent;
        }else if(grand.isRightChild()){
            grand.parent.right = parent;
        }else{
            // 如果grand的父节点是空,那么说明,grand原来是root节点,那么就需要将root节点指向parent节点
            root = parent;
        }

        // 2. 更新节点的parent属性
        if(child != null){
            child.parent = grand;
        }
        grand.parent = parent;
    }

}
