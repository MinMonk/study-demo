package com.monk.leetcode.november;

/**
 * 使用双向链表表示顺序二叉树,并完成二叉树的遍历(前序打印,中序打印,后序打印)
 */
public class Demo17Three {

    public static void main(String[] args) {
        DoubleTree tree = new DoubleTree();
        tree.add(4);
        tree.add(7);
        tree.add(2);
        tree.add(1);
        tree.add(6);
        tree.add(8);
        tree.add(3);
        tree.add(9);
        tree.add(14);

        //遍历二叉树,该方法内部实现了前序,中序,后序遍历二叉树
        tree.loop();
        System.out.println("======");
        tree.levelOrderTraversal();
    }


}
