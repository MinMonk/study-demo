package com.monk.er_cha_shu;

import java.util.*;

/**
 * https://leetcode-cn.com/problems/binary-tree-level-order-traversal-ii/
 * <p>
 * 107. 二叉树的层序遍历 II
 */
public class _107_ {

    public static void main(String[] args) {
        TreeNode tree = TreeNode.buildLevelData();
        _107_ demo = new _107_();
        /**
         * <pre>
         * 此题有两种解法:
         *  解法一: 参照102的层序遍历,然后将结果反转即可
         *  解法二: 利用LinkedList的链表结构,每次添加一层数据的时候,都插入到List的首位置
         *  解法三: ArrayList使用数组存储也可以每次往首位置插入,但是ArrayList在插入
         *      首位置的时候,需要移动元素,会在性能上有损耗,故使用LinkedList较好
         *  </pre>
         */
        List<List<Integer>> list = demo.levelOrder(tree);
        list.forEach(System.out::println);
        System.out.println("==============");
        List<List<Integer>> list2 = demo.levelOrder2(tree);
        list2.forEach(System.out::println);
    }

    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> list = new ArrayList<>();
        if (root == null) return list;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int queueSize = 1;
        List<Integer> level = new ArrayList<>();
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            queueSize--;

            level.add(node.val);
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
            if (queueSize == 0) {
                list.add(level);
                level = new ArrayList<>();
                queueSize = queue.size();
            }
        }

        List<List<Integer>> newList = new ArrayList<>(list.size());
        for (int i = 1; i <= list.size(); i++) {
            newList.add(list.get(list.size() - i));
        }

        return newList;
    }

    public List<List<Integer>> levelOrder2(TreeNode root) {
        List<List<Integer>> list = new LinkedList<>();
        if (root == null) return list;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int queueSize = 1;
        List<Integer> level = new ArrayList<>();
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            queueSize--;

            level.add(node.val);
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
            if (queueSize == 0) {
                list.add(0, level);
                level = new ArrayList<>();
                queueSize = queue.size();
            }
        }

        return list;
    }

}
