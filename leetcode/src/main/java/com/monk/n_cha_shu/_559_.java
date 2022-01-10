package com.monk.n_cha_shu;

/**
 * https://leetcode-cn.com/problems/maximum-depth-of-n-ary-tree/
 *
 * 559. N 叉树的最大深度
 */
public class _559_ {

    public static void main(String[] args) {
        Node root = Node.buildData();
        _559_ demo = new _559_();
        System.out.println(demo.maxDepth(root));
    }

    public int maxDepth(Node root) {
        if(root == null) return 0;


        return 0;
    }
}
