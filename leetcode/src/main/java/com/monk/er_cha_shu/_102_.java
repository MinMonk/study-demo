package com.monk.er_cha_shu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * https://leetcode-cn.com/problems/binary-tree-level-order-traversal/
 * <p>
 * 102. 二叉树的层序遍历
 */
public class _102_ {

    public static void main(String[] args) {
        TreeNode tree = TreeNode.buildLevelData();
        _102_ demo = new _102_();
        List<List<Integer>> list = demo.levelOrder(tree);
        list.forEach(System.out::println);
    }

    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> list = new ArrayList<>();
        if (root == null) return list;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int queueSize = 1;
        List<Integer> level = new ArrayList<>();
        while (!queue.isEmpty()){
            TreeNode node = queue.poll();
            queueSize--;

            level.add(node.val);
            if(node.left != null){
                queue.offer(node.left);
            }
            if(node.right != null){
                queue.offer(node.right);
            }
            if(queueSize == 0){
                list.add(level);
                level = new ArrayList<>();
                queueSize = queue.size();
            }
        }

        return list;
    }

}
