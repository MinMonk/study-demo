package com.monk.er_cha_shu;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode() {
    }

    TreeNode(int val) {
        this.val = val;
    }

    public boolean hasTwoLeaf(){
        return left != null && right != null;
    }

    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
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

    public static TreeNode buildData(){
        TreeNode tree = new TreeNode();
        tree.val = 5;
        tree.left = new TreeNode(3);
        tree.left.left = new TreeNode(2);
        tree.left.right = new TreeNode(4);

        tree.right = new TreeNode(6);
        tree.right.right = new TreeNode(7);
        tree.right.left = null;

        return tree;
    }

    public static TreeNode buildLevelData(){
        TreeNode tree = new TreeNode();
        tree.val = 3;
        tree.left = new TreeNode(9);;
        tree.right = new TreeNode(20);
        tree.right.left = new TreeNode(15);
        tree.right.right = new TreeNode(7);

        return tree;
    }

}
