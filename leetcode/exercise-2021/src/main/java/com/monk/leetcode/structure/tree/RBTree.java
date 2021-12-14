package com.monk.leetcode.structure.tree;


import java.util.Comparator;

/**
 * 红黑树
 * @param <E>
 */
public class RBTree<E> extends BalanceTree<E> {

    private static final boolean RED = false;
    private static final boolean BLACK = true;

    public RBTree(){}
    public RBTree(Comparator<E> comparator) {
        super(comparator);}

    @Override
    protected void afterAdd(Node<E> node) {
        Node<E> parent = node.parent;

        // 如果父节点为空,则添加的是根节点,直接染成黑色返回即可(上溢到root节点的话,也视作是添加根节点)
        if(parent == null){
            black(node);
            return;
        }

        // 如果父节点是黑色,就不用做处理
        if(isBlack(parent)) return;

        Node<E> uncle = parent.sibling();
        Node<E> grand = red(parent.parent);

        /**
         * 处理叔父节点是红色的情况
         * 1. 将parent和uncle节点染成黑色
         * 2. 将grand节点上溢
         */
        if(isRed(uncle)){
            black(parent);
            black(uncle);

            // grand节点上溢(将grand节点当做是一个新添加的节点来递归添加,因为上溢可能会导致祖先节点出现上溢)
            afterAdd(grand);
            return;
        }

        /**
         *
         * 处理叔父节点不是红色的情况
         * <pre>
         *     分析: 当叔父节点不是红色的情况下,也分为4中情况(LL,LR,RL,RR),但是这四种情况和AVL树有些区别,
         *      1. LL : 将parent节点染成黑色,grand节点染成红色,再对grand节点进行右旋转
         *      2. LR : 将node节点(自己)染成黑色,grand节点染成红色,再对parent节点左旋转,grand节点进行右旋转
         *      3. RL : 将node节点(自己)染成黑色,grand节点染成红色,再对parent节点右旋转,grand节点进行左旋转
         *      4. RR : 将parent节点染成黑色,grand节点染成红色,再对grand节点进行左旋转
         *  注意:
         *      下述代码中,
         *      1. 由于都需要对grand节点进行染成红色,故在29行拿到grand节点的时候,就已经对其进行了染色操作
         *      2. 针对LL和LR情况,均需要对grand节点进行右旋转,故提出了公共代码放在了if-else外(Line:68)
         *      3. 针对RL和RR情况,都需要对grand节点进行左旋转,故也提出了公共代码放在了if-else外(Line:76)
         * </pre>
         */
        if(parent.isLeftChild()){//L
            if(node.isLeftChild()){//LL
                black(parent);
            }else{//LR
                black(node);
                rotateLeft(parent);
            }
            rotateRight(grand);
        }else{//R
            if(node.isLeftChild()){//RL
                black(node);
                rotateRight(parent);
            }else{//RR
                black(parent);
            }
            rotateLeft(grand);
        }
    }

    @Override
    protected void afterRemove(Node<E> node) {
        // 如果删除的节点是红色
        // 或者 用以取代删除节点的子节点是红色
        if (isRed(node)) {
            black(node);
            return;
        }

        Node<E> parent = node.parent;
        // 删除的是根节点
        if (parent == null) return;

        // 删除的是黑色叶子节点【下溢】
        // 判断被删除的node是左还是右
        boolean left = parent.left == null || node.isLeftChild();
        Node<E> sibling = left ? parent.right : parent.left;
        if (left) { // 被删除的节点在左边，兄弟节点在右边
            if (isRed(sibling)) { // 兄弟节点是红色
                black(sibling);
                red(parent);
                rotateLeft(parent);
                // 更换兄弟
                sibling = parent.right;
            }

            // 兄弟节点必然是黑色
            if (isBlack(sibling.left) && isBlack(sibling.right)) {
                // 兄弟节点没有1个红色子节点，父节点要向下跟兄弟节点合并
                boolean parentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if (parentBlack) {
                    afterRemove(parent);
                }
            } else { // 兄弟节点至少有1个红色子节点，向兄弟节点借元素
                // 兄弟节点的左边是黑色，兄弟要先旋转
                if (isBlack(sibling.right)) {
                    rotateRight(sibling);
                    sibling = parent.right;
                }

                color(sibling, colorOf(parent));
                black(sibling.right);
                black(parent);
                rotateLeft(parent);
            }
        } else { // 被删除的节点在右边，兄弟节点在左边
            if (isRed(sibling)) { // 兄弟节点是红色
                black(sibling);
                red(parent);
                rotateRight(parent);
                // 更换兄弟
                sibling = parent.left;
            }

            // 兄弟节点必然是黑色
            if (isBlack(sibling.left) && isBlack(sibling.right)) {
                // 兄弟节点没有1个红色子节点，父节点要向下跟兄弟节点合并
                boolean parentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if (parentBlack) {
                    afterRemove(parent);
                }
            } else { // 兄弟节点至少有1个红色子节点，向兄弟节点借元素
                // 兄弟节点的左边是黑色，兄弟要先旋转
                if (isBlack(sibling.left)) {
                    rotateLeft(sibling);
                    sibling = parent.left;
                }

                color(sibling, colorOf(parent));
                black(sibling.left);
                black(parent);
                rotateRight(parent);
            }
        }

    }

    @Override
    protected Node<E> createNode(E element, Node<E> parent) {
        return new RBNode(element, parent);
    }

    /**
     * 对传进来的节点进行染色
     * @param node
     * @param color
     * @return
     */
    private Node<E> color(Node<E> node, boolean color){
        if(node == null) return node;
        ((RBNode)node).color = color;
        return node;
    }

    /**
     * 将节点染成红色
     * @param node
     * @return
     */
    private Node<E> red(Node<E> node){
        return color(node, RED);
    }

    /**
     * 将节点染成黑色
     * @param node
     * @return
     */
    private Node<E> black(Node<E> node){
        return color(node, BLACK);
    }

    /**
     * 返回传入节点的颜色(如果是空节点,返回的黑色)
     * @param node
     * @return
     */
    private boolean colorOf(Node<E> node){
        return node == null ? BLACK : ((RBNode)node).color;
    }

    /**
     * 判断节点的颜色是否是红色
     * @param node
     * @return
     */
    private boolean isRed(Node<E> node){
        return colorOf(node) == RED;
    }

    /**
     * 判断节点的颜色是否是黑色
     * @param node
     * @return
     */
    private boolean isBlack(Node<E> node){
        return colorOf(node) == BLACK;
    }


    /**
     * 红黑树Node节点
     * @param <E>
     */
    private static class RBNode<E> extends Node<E>{

        boolean color = RED;

        public RBNode(E element, Node<E> parent) {
            super(element, parent);
        }

        @Override
        public String toString() {
            return (color ? "(" : "R(") + element + ")";
        }
    }
}
