package com.monk.leetcode.structure.tree;


import com.monk.leetcode.structure.tree.printer.BinaryTrees;

public class TreeTest {

    public static void main(String[] args) {
        //testAdd();

        orderPrint();

        // testHeight();

        // testIsComplete();

        // testPredecessor();

        // testSuccessor();

        // testRemove();

        // testAVLTree();

        // testRBTree();
    }

    /**
     * 测试红黑树
     */
    private static void testRBTree() {
        RBTree<Integer> avl = new RBTree<>();
        int[] arrs = new int[]{
                23, 64, 77, 34, 31, 44, 19, 49, 60, 4, 83, 79, 89
        };
        for (int i = 0; i < arrs.length; i++) {
            avl.add(arrs[i]);
            System.out.println("【" + arrs[i] + "】");
            BinaryTrees.println(avl);
            System.out.println("---------------------------------------");
        }

        // avl.remove(77);
        BinaryTrees.println(avl);
    }

    /**
     * 测试平衡二叉树
     */
    private static void testAVLTree() {
        AVLTree<Integer> avl = new AVLTree<>();
        int[] arrs = new int[]{
                23, 64, 77, 34, 31, 44, 19, 49, 60, 4, 83, 79, 89
        };
        for (int i = 0; i < arrs.length; i++) {
            avl.add(arrs[i]);
            System.out.println("【" + arrs[i] + "】");
			BinaryTrees.println(avl);
			System.out.println("---------------------------------------");
        }

        // avl.remove(77);
        BinaryTrees.println(avl);
    }

    private static void testRemove() {
        BSTree<Integer> tree = generateBasicTree();
        BinaryTrees.println(tree);

        // 删除度为0的元素
        // tree.remove(1);
        // tree.remove(3);
        // tree.remove(12);

        // 删除度为1的元素
        // tree.remove(11);

        // 删除度为2的元素
        tree.remove(2);
        tree.remove(9);
        tree.remove(7);

        System.out.println("=====================");
        System.out.println("-----   删除后   -----");
        System.out.println("=====================");
        BinaryTrees.println(tree);
    }

    /**
     * 测试获取某一个节点的后驱节点(如果需要测试该方法,将二叉树中的这个方法改为public)
     */
    private static void testSuccessor() {
        BSTree<Integer> tree = generateBasicTree();
        BinaryTrees.println(tree);

        BSTree.Node<Integer> node8 = tree.successor(tree.getRoot());
        System.out.println("根节点的后驱节点是" + node8);
        BSTree.Node<Integer> node9 = tree.successor(node8);
        System.out.println("节点[" + node8 + "]的后前驱节点是" + node9);
        BSTree.Node<Integer> node11 = tree.successor(node9);
        System.out.println("节点[" + node9 + "]的后驱节点是" + node11);
    }

    /**
     * 测试获取某一个节点的前驱节点(如果需要测试该方法,将二叉树中的这个方法改为public)
     */
    private static void testPredecessor() {
        BSTree<Integer> tree = generateBasicTree();
        BinaryTrees.println(tree);

        BSTree.Node<Integer> node7 = new BSTree.Node<Integer>(7, null);

        BSTree.Node<Integer> node5 = tree.predecessor(tree.getRoot());
        System.out.println("节点[" + node7 + "]的前驱节点是" + node5);
        BSTree.Node<Integer> node4 = tree.predecessor(node5);
        System.out.println("节点[" + node5 + "]的前驱节点是" + node4);
        BSTree.Node<Integer> node3 = tree.predecessor(node4);
        System.out.println("节点[" + node4 + "]的前驱节点是" + node3);
    }

    /**
     * 测试一棵树是否是一颗完全二叉树
     */
    private static void testIsComplete() {
        BSTree<Integer> tree = generateBasicTree();
        BinaryTrees.println(tree);
        System.out.println("当前这个树[" + (tree.isComplete() ? "是" : "不是") + "]一颗完全二叉树");

        System.out.println("================");
        int[] arrs = new int[]{7, 4, 9, 2, 5};
        BSTree<Integer> tree2 = new BSTree<>();
        for (int i = 0; i < arrs.length; i++) {
            tree2.add(arrs[i]);
        }
        BinaryTrees.println(tree2);
        System.out.println("当前这个树[" + (tree2.isComplete() ? "是" : "不是") + "]一颗完全二叉树");
    }

    /**
     * 测试获取树的高度
     */
    private static void testHeight() {
        BSTree<Integer> tree = generateBasicTree();
        BinaryTrees.println(tree);

        System.out.println("非递归方式获取到树的高度height = " + tree.height());
        System.out.println("递归方式获取到树的高度height = " + tree.height2());
    }

    /**
     * 测试前/中/后序打印以及层级打印
     */
    private static void orderPrint() {
        BSTree<Integer> tree = generateBasicTree();
        BinaryTrees.println(tree);

        BinaryTree.Visitor<Integer> visitor = new BinaryTree.Visitor(){
            @Override
            public boolean visit(Object element) {
                System.out.print(element + ",");
                return false;
            }
        };

        // 递归前序打印
        tree.preOrder(visitor);
        System.out.println();

        // 非递归前序打印
        tree.preOrder2(visitor);
        System.out.println();

        // 非递归前序打印
        tree.preOrder3(visitor);
        System.out.println();

        // 中序打印(递归实现)
        tree.inOrder(visitor);
        System.out.println();

        // 中序打印(非递归实现)
        tree.inOrder2(visitor);
        System.out.println();

        // 后序打印(递归实现)
        tree.postOrder(visitor);
        System.out.println();

        // 后序打印(非递归实现)
        tree.postOrder2(visitor);
        System.out.println();

        // 层序打印
        tree.levelOrder(visitor);
    }

    /**
     * 测试添加方法
     */
    private static void testAdd() {
        BSTree<Integer> tree = generateBasicTree();

        BinaryTrees.println(tree);
    }

    /**
     * 生成一个基础的测试数据二叉树
     *
     * @return
     */
    private static BSTree<Integer> generateBasicTree() {
        BSTree<Integer> tree = new BSTree<>();
        int[] arrs = new int[]{7, 4, 9, 2, 5, 8, 11, 3, 12, 1};
        for (int i = 0; i < arrs.length; i++) {
            tree.add(arrs[i]);
        }
        return tree;
    }
}
