package com.monk.leetcode.structure.tree;


import com.monk.leetcode.structure.tree.printer.BinaryTreeInfo;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * 二叉树
 *
 * @param <E>
 */
public class BinaryTree<E> implements BinaryTreeInfo {

    protected static class Node<E> {
        protected E element;
        protected Node<E> left;
        protected Node<E> right;
        protected Node<E> parent;

        public Node(E element, Node<E> parent) {
            this.element = element;
            this.parent = parent;
        }

        /**
         * 是否是度为2的节点
         *
         * @return
         */
        public boolean hasTwoLeaf() {
            return left != null && right != null;
        }

        /**
         * 判断是否是叶子节点(叶子节点:既没有左节点,也没有右节点)
         *
         * @return
         */
        public boolean isLeaf() {
            return left == null && right == null;
        }

        /**
         * 是否是左节点
         *
         * @return
         */
        public boolean isLeftChild() {
            return parent != null && this == parent.left;
        }

        /**
         * 是否是右节点
         *
         * @return
         */
        public boolean isRightChild() {
            return parent != null && this == parent.right;
        }

        /**
         * 返回当前节点的兄弟节点
         *
         * @return
         */
        public Node<E> sibling() {
            if (isLeftChild()) {
                return parent.right;
            }

            if (isRightChild()) {
                return parent.left;
            }

            return null;
        }

        @Override
        public String toString() {
            return element.toString();
        }
    }

    protected static abstract class Visitor<E> {
        boolean stop;

        public abstract boolean visit(E element);
    }

    protected int size;

    protected Node root;

    /**
     * 返回树中元素的个数
     *
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * 判断树中是否有元素
     *
     * @return
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 清空整棵树
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * 前序打印3(非递归方式实现)
     */
    public void preOrder3(Visitor<E> visitor) {
        if (null == root || null == visitor) return;

        Stack<Node<E>> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Node<E> node = stack.pop();
            if (visitor.visit(node.element)) return;

            /**
             * 虽然和层序遍历的代码差不多,但是要注意入栈的顺序,要先入右边节点再入左边节点
             * 层序遍历的时候是先左再右
             */
            if (null != node.right) {
                stack.push(node.right);
            }
            if (null != node.left) {
                stack.push(node.left);
            }
        }
    }

    /**
     * 前序打印2(非递归方式实现)
     */
    public void preOrder2(Visitor<E> visitor) {
        if (null == root || null == visitor) return;

        Node<E> node = root;
        Stack<Node<E>> stack = new Stack<>();
        while (true) {
            if (null != node) {
                if (visitor.visit(node.element)) return;

                if (null != node.right) {
                    stack.push(node.right);
                }
                node = node.left;
            } else if (stack.isEmpty()) {
                return;
            } else {
                node = stack.pop();
            }
        }
    }

    /**
     * 前序打印
     */
    public void preOrder(Visitor<E> visitor) {
        if (null == visitor) return;
        preOrder(root, visitor);
    }

    private void preOrder(Node<E> node, Visitor<E> visitor) {
        if (null == node || visitor.stop) return;

        /**
         * 接收用户自定义打印之后是否还继续打印的boolean值
         * 区别于中序和后序,不用再次判断visitor.stop属性是因为,
         * 这是前序打印,在这个方法的第一行已经判断过了,就不需要再次判断了
         */
        visitor.stop = visitor.visit(node.element);
        preOrder(node.left, visitor);
        preOrder(node.right, visitor);
    }

    /**
     * 中序打印
     */
    public void inOrder2(Visitor<E> visitor) {
        if (null == root || null == visitor) return;

        Node<E> node = root;
        Stack<Node<E>> stack = new Stack<>();
        while (true) {
            if (null != node) {
                stack.push(node);
                node = node.left;
            } else if (stack.isEmpty()) {
                return;
            } else {
                node = stack.pop();
                if (visitor.visit(node.element)) return;

                node = node.right;
            }
        }
    }

    /**
     * 中序打印
     */
    public void inOrder(Visitor<E> visitor) {
        if (null == visitor) return;
        inOrder(root, visitor);
    }

    private void inOrder(Node<E> node, Visitor<E> visitor) {
        if (null == node || visitor.stop) return;
        inOrder(node.left, visitor);

        // 这里再判断一次是为了避免再次进入递归
        if (visitor.stop) return;
        // 接收用户自定义打印之后是否还继续打印的boolean值
        visitor.stop = visitor.visit(node.element);
        inOrder(node.right, visitor);
    }

    /**
     * 后序打印(非递归方式实现)
     */
    public void postOrder2(Visitor<E> visitor) {
        if (null == root || null == visitor) return;

        Node<E> prev = null;
        Stack<Node<E>> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Node<E> top = stack.peek();
            if (top.isLeaf() || (prev != null && prev.parent == top)) {
                prev = stack.pop();

                if (visitor.visit(prev.element)) return;
            } else {
                // 注意入栈顺序不能调换,要先右后左
                if (top.right != null) {
                    stack.push(top.right);
                }
                if (top.left != null) {
                    stack.push(top.left);
                }
            }
        }
    }

    /**
     * 后序打印
     */
    public void postOrder(Visitor<E> visitor) {
        if (null == visitor) return;
        postOrder(root, visitor);
    }

    private void postOrder(Node<E> node, Visitor<E> visitor) {
        if (null == node || visitor.stop) return;
        postOrder(node.left, visitor);
        postOrder(node.right, visitor);

        // 这里再判断一次是为了避免再次进入递归
        if (visitor.stop) return;
        // 接收用户自定义打印之后是否还继续打印的boolean值
        visitor.stop = visitor.visit(node.element);
    }

    /**
     * 层级打印
     */
    public void levelOrder(Visitor<E> visitor) {
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            //System.out.print(node.element + ",");
            if (visitor.visit(node.element)) return;

            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
    }

    /**
     * 获取二叉树的高度(非递归方式)
     *
     * @return
     */
    public int height() {
        if (root == null) return 0;

        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        int queueSize = 1;
        int height = 0;
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            queueSize--;

            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
            if (queueSize == 0) {
                height++;
                queueSize = queue.size();
            }
        }
        return height;
    }

    /**
     * 获取二叉树的高度(递归方式)
     *
     * @return
     */
    public int height2() {
        return height(root);
    }

    public int height(Node<E> node) {
        if (node == null) return 0;

        return 1 + Math.max(height(node.left), height(node.right));
    }

    /**
     * 是否是完全二叉树
     * <pre>
     *     完全二叉树的概念：叶子节点只会出现在最后2层，且最后一层的节点都靠左对齐
     *     分析:
     *         根据概念可知,一颗完全二叉树的有下面几种情况(这里用层级遍历的方式进行判断,借助队列)
     *         1. 如果根节点为null,那是一颗空树,直接返回false
     *         2. 如果某一个节点的left节点为空,但是right节点不为空,也可以直接返回false
     *         3. 如果某一个节点的left和right节点均不为null,那么需要将这个节点加入队列,再继续判断
     *         4. 如果某一个节点的(left!=null && right == null) 或 (left == null && right == null),说明那么
     *               他后面的节点都必须是叶子节点,如果碰到了不是叶子节点,那么就直接返回false,说明这棵树不是一颗完全二叉树(这一
     *               点可能有点难以理解,但是画一个完全二叉树再加上按照层级打印的思路去理解就不难理解了)
     *         5. 如果通过上面的判断,都通过了判断,那么这棵树就是一颗完全二叉树
     * </pre>
     *
     * @return
     */
    public boolean isComplete() {
        if (root == null) return false;

        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        boolean isLeaf = false;
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();

            if (isLeaf && !node.isLeaf()) {
                /**
                 * 如果标志位isLeaf=true说明要求当前遍历的节点必须是叶子节点,否则这棵树就不是一颗完全二叉树(根据该方法注释中的分析第4点来判断)
                 */
                return false;
            }

            if (node.left != null) {
                queue.offer(node.left);
            } else {
                // 如果进入了这个else分支,说明left节点为空,那么如果在这个前提下,right节点不为空,则根据该方法注释中的分析第二点则可以说明这棵树不是一颗完全二叉树
                if (node.right != null) return false;
            }

            if (node.right != null) {
                queue.offer(node.right);
            } else {
                /**
                 * 如果进入这个分支,说明right节点为空,那么在这个前提下,如果左节点无论是否为空,则
                 * 后序遍历到的节点都必须是叶子节点,否则根据该方法注释中的分析第4点则可以说明这棵树不是一颗完全二叉树
                 */
                isLeaf = true;
            }
        }

        return true;
    }

    /**
     * 获取节点的前驱节点(中序遍历)
     *
     * @param node
     * @return
     */
    protected Node<E> predecessor(Node<E> node) {
        if (node == null) return null;

        Node<E> pre = node.left;
        if (pre != null) {
            // 如果该节点的left不为空,则它的前驱节点为left.right.right.right....
            while (pre.right != null) {
                pre = pre.right;
            }
            return pre;
        }

        /**
         * 如果该节点的left为空,则它的前驱节点为parent.parent.(...).right,
         * 所以当父节点为空or当前节点不等于父节点的右边节点时,跳出循环
         */
        while (node.parent != null && !node.equals(node.parent.right)) {
            node = node.parent;
        }

        return node.parent;
    }

    /**
     * 获取节点的后继节点(中序遍历)
     *
     * @param node
     * @return
     */
    protected Node<E> successor(Node<E> node) {
        if (node == null) return null;

        Node<E> suc = node.right;
        if (suc != null) {
            while (suc.left != null) {
                suc = suc.left;
            }
            return suc;
        }

        /**
         * 如果该节点的right为空,则它的前驱节点为parent.parent.(...).left,
         * 所以当父节点为空or当前节点不等于父节点的左边节点时,跳出循环
         */
        while (node.parent != null && !node.equals(node.parent.left)) {
            node = node.parent;
        }

        return node.parent;
    }

    @Override
    public Object root() {
        return root;
    }

    @Override
    public Object left(Object node) {
        return ((Node<E>) node).left;
    }

    @Override
    public Object right(Object node) {
        return ((Node<E>) node).right;
    }

    @Override
    public Object string(Object node) {
        /*BinaryTree.Node<E> myNode = (BinaryTree.Node<E>)node;
        String parentString = "null";
        if (myNode.parent != null) {
            parentString = myNode.parent.element.toString();
        }
        return myNode.element + "_p(" + parentString + ")";*/
        return node.toString();
    }
}
