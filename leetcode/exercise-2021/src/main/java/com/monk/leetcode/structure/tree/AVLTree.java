package com.monk.leetcode.structure.tree;

/**
 * 平衡二叉搜素树
 */
public class AVLTree<E> extends BalanceTree<E> {

    /**
     * 平衡二叉树的节点,扩展二叉树Node节点,添加height属性
     * @param <E>
     */
    protected static class AVLNode<E> extends Node<E> {
        /**
         * 节点高度
         */
        private int height = 1;

        public AVLNode(E element, Node<E> parent) {
            super(element, parent);
        }

        /**
         * 求当前节点的平衡因子
         * @return
         */
        public int balanceFactor() {
            int leftHeight = left != null ? ((AVLNode<E>) left).height : 0;
            int rightHeight = right != null ? ((AVLNode<E>) right).height : 0;
            return leftHeight - rightHeight;
        }

        /**
         * 更新当前节点的高度
         *
         * 当前节点的高度等于左右子树的最大高度加1
         */
        public void updateHeight() {
            int leftHeight = left != null ? ((AVLNode<E>) left).height : 0;
            int rightHeight = right != null ? ((AVLNode<E>) right).height : 0;
            height =  1 + Math.max(leftHeight,rightHeight);
        }

        /**
         * 返回更高的子节点
         * @return
         */
        public Node<E> tallerChild(){
            int leftHeight = left != null ? ((AVLNode<E>) left).height : 0;
            int rightHeight = right != null ? ((AVLNode<E>) right).height : 0;

            /**
             * 1. 谁高就返回哪个孩子节点
             * 2. 如果两个孩子节点的高度相等,就看当前节点是在父节点的左边还是右边,保持统一
             * 如果是父节点的left就返回left,反之返回right
             */
            if(leftHeight > rightHeight) return left;
            if(rightHeight > leftHeight) return right;
            return isLeftChild() ? left : right;
        }

        @Override
        public String toString() {
            String pStr = parent == null ? "null" : parent.element.toString();
            return element + "_p(" + pStr + ")_h(" + height + ")";
        }
    }

    @Override
    protected Node<E> createNode(E element, Node<E> parent) {
        return new AVLNode<>(element, parent);
    }

    @Override
    protected void afterAdd(Node<E> node) {
        while ((node = node.parent) != null) {
            if (isBalance(node)) {
                // 如果当前节点平衡,则更新其高度即可
                updateHeight(node);

            } else {
                /**
                 * <pre>
                 * 如果当前节点不平衡就进行对应的平衡操作
                 *  注意:
                 *    这里调用reBalanced方法传参的node节点并不是要添加的那个节点,而是添加操作完成后
                 *    让二叉树不平衡的那个最低的祖父节点
                 * </pre>
                 */
                reBalanced(node);
                break;
            }
        }
    }

    @Override
    protected void afterRemove(Node<E> node) {
        while ((node = node.parent) != null) {
            if (isBalance(node)) {
                // 如果当前节点平衡,则更新其高度即可
                updateHeight(node);

            } else {
                /**
                 * <pre>
                 * 如果当前节点不平衡就进行对应的平衡操作
                 *  注意:
                 *    这里调用reBalanced方法传参的node节点并不是要添加的那个节点,而是添加操作完成后
                 *    让二叉树不平衡高度最低的祖父节点
                 * </pre>
                 */
                reBalanced(node);
            }
        }
    }

    /**
     * 恢复平衡
     *
     * <pre>
     *      二叉树的平衡操作,主要分为LL,LR,RR,RL四种旋转操作
     *          1. LL : 旋转1次 : 右旋转祖父节点
     *          2. LR : 旋转2次 : 先左旋转parent节点,再右旋转祖父节点
     *          3. RR : 旋转1次 : 左旋转祖父节点
     *          4. RL : 旋转2次 : 先右旋转parent节点,再左旋转祖父节点
     * </pre>
     *
     * @param grand  祖父节点
     */
    private void reBalanced(Node<E> grand){
        Node<E> parent = ((AVLNode<E>)grand).tallerChild();
        Node<E> node = ((AVLNode<E>) parent).tallerChild();
        if(parent.isLeftChild()){
            if(node.isLeftChild()){
                // LL : 旋转1次 : 右旋转祖父节点
                rotateRight(grand);
            }else{
                // LR : 旋转2次 : 先左旋转parent节点,再右旋转祖父节点
                rotateLeft(parent);
                rotateRight(grand);
            }
        }else{
            if(node.isLeftChild()){
                // RL : 旋转2次 : 先右旋转parent节点,再左旋转祖父节点
                rotateRight(parent);
                rotateLeft(grand);
            }else{
                // RR : 旋转1次 : 左旋转祖父节点
                rotateLeft(grand);
            }
        }
    }

    @Override
    protected void afterRotate(Node<E> grand, Node<E> parent, Node<E> child) {
        super.afterRotate(grand, parent, child);

        // 3. 更新节点的高度
        updateHeight(grand);
        updateHeight(parent);
    }

    /**
     * 判断节点是否平衡
     *
     * @param node
     * @return
     */
    private boolean isBalance(Node<E> node) {
        return Math.abs(((AVLNode<E>)node).balanceFactor()) <= 1;
    }

    /**
     * 更新节点的高度
     * @param node
     */
    private void updateHeight(Node<E> node) {
        ((AVLNode<E>) node).updateHeight();
    }
}
