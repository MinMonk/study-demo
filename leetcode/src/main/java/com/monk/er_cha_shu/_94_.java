package com.monk.er_cha_shu;

import java.util.ArrayList;
import java.util.List;

/**
 * https://leetcode-cn.com/problems/binary-tree-inorder-traversal/
 * <p>
 * 94. 二叉树的中序遍历
 */
public class _94_ {

    public static void main(String[] args) {
        TreeNode tree = TreeNode.buildData();
        _94_ demo = new _94_();
        List<Integer> list = demo.inorderTraversal(tree);
        list.forEach(System.out::println);
    }

    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        inOrder(root, list);
        return list;
    }

    private void inOrder(TreeNode node, List<Integer> list) {
        if (node == null) return ;

        inOrder(node.left, list);
        list.add(node.val);
        inOrder(node.right, list);
    }
}
