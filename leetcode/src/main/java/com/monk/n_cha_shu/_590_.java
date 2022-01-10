package com.monk.n_cha_shu;

import java.util.*;

/**
 * https://leetcode-cn.com/problems/n-ary-tree-postorder-traversal/
 *
 * 590. N 叉树的后序遍历
 */
public class _590_ {

    public static void main(String[] args) {
        Node root = Node.buildData();
        _590_ demo = new _590_();
        List<Integer> list = demo.postorder(root);
        list.forEach(e -> {
            System.out.print(e + ",");
        });
    }

    public List<Integer> postorder(Node root) {
        LinkedList<Integer> res = new LinkedList<>();
        if (root == null) {
            return res;
        }

        Deque<Node> stack = new ArrayDeque<>();
        stack.addLast(root);
        while (!stack.isEmpty()) {
            Node node = stack.removeLast();
            res.addFirst(node.val);
            if(node.children == null) continue;
            for (int i = 0; i < node.children.size(); i++) {
                stack.addLast(node.children.get(i));
            }
        }
        return res;
    }
}
