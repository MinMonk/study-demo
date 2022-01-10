package com.monk.n_cha_shu;

import java.util.ArrayList;
import java.util.List;

public class Node {
    public int val;
    public List<Node> children;

    public Node() {}

    public Node(int _val) {
        val = _val;
    }

    public Node(int _val, List<Node> _children) {
        val = _val;
        children = _children;
    }

    public static Node buildData(){
        List<Node> c3_1 = new ArrayList<>();
        c3_1.add(new Node(5));
        c3_1.add(new Node(7));
        c3_1.add(new Node(6));
        Node n3_1 = new Node(3, c3_1);

        List<Node> c3_2 = new ArrayList<>();
        c3_2.add(new Node(9));
        c3_2.add(new Node(10));
        c3_2.add(new Node(11));
        Node n3_2 = new Node(2, c3_2);

        Node n3_3 = new Node(4);

        List<Node> c2 = new ArrayList<>();
        c2.add(n3_1);
        c2.add(n3_2);
        c2.add(n3_3);

        Node root = new Node(1, c2);

        return root;
    }
};
