package com.monk.er_cha_shu;

import java.util.ArrayList;
import java.util.List;

/**
 * https://leetcode-cn.com/problems/binary-tree-postorder-traversal/
 * <p>
 * 145. 二叉树的后序遍历
 */
public class _145_ {

    public static void main(String[] args) {
        TreeNode tree = TreeNode.buildData();
        _145_ demo = new _145_();
        List<Integer> list = demo.postorderTraversal(tree);
        list.forEach(System.out::println);
    }

    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        postOrder(root, list);
        return list;
    }

    private void postOrder(TreeNode node, List<Integer> list) {
        if (node == null) return ;

        postOrder(node.left, list);
        postOrder(node.right, list);
        list.add(node.val);
    }
}
