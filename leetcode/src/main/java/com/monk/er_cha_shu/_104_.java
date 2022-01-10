package com.monk.er_cha_shu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * https://leetcode-cn.com/problems/maximum-depth-of-binary-tree/
 * <p>
 * 104. 二叉树的最大深度
 */
public class _104_ {

    public static void main(String[] args) {
        TreeNode tree = TreeNode.buildLevelData();
        _104_ demo = new _104_();
        System.out.println(demo.maxDepth(tree));
    }

    public int maxDepth(TreeNode root) {
        int depth = 0;
        if (root == null) return depth;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int queueSize = 1;
        List<Integer> level = new ArrayList<>();
        while (!queue.isEmpty()){
            TreeNode node = queue.poll();
            queueSize--;

            if(node.left != null){
                queue.offer(node.left);
            }
            if(node.right != null){
                queue.offer(node.right);
            }
            if(queueSize == 0){
                depth++;
                queueSize = queue.size();
            }
        }

        return depth;
    }

}
