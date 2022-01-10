package com.monk.er_cha_shu;

import java.util.ArrayList;
import java.util.List;

/**
 * https://leetcode-cn.com/problems/binary-tree-preorder-traversal/
 * <p>
 * 144. 二叉树的前序遍历
 */
public class _144_ {

    public static void main(String[] args) {
        TreeNode tree = TreeNode.buildData();
        _144_ demo = new _144_();
        List<Integer> list = demo.preorderTraversal(tree);
        list.forEach(System.out::println);
    }

    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        preOrder(root, list);
        return list;
    }

    private void preOrder(TreeNode node, List<Integer> list) {
        if (node == null) return ;

        list.add(node.val);
        preOrder(node.left, list);
        preOrder(node.right, list);
    }
}
