package com.monk.n_cha_shu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * https://leetcode-cn.com/problems/n-ary-tree-preorder-traversal/
 *
 * 589. N 叉树的前序遍历
 */
public class _589_ {

    public static void main(String[] args) {
        Node root = Node.buildData();
        _589_ demo = new _589_();
        List<Integer> list = demo.preorder(root);
        list.forEach(e -> {
            System.out.print(e + ",");
        });
    }

    public List<Integer> preorder(Node root) {
        List<Integer> list = new ArrayList<>();
        if(root == null) return list;

        LinkedList<Node> stack = new LinkedList<>();
        stack.add(root);
        while (!stack.isEmpty()){
            Node node = stack.pollLast();
            list.add(node.val);
            List<Node> childs = node.children;
            if(childs!=null && !childs.isEmpty()){
                Collections.reverse(childs);
                for(Node n : childs){
                    stack.add(n);
                }
            }
        }

        return list;
    }
}
