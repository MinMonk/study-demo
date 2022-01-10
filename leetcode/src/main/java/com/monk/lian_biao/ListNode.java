package com.monk.lian_biao;

/**
 * leetcode链表对象
 */
public class ListNode {
    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int val) {
        this.val = val;
    }

    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }

    /**
     * 构建初始化数据
     * @return
     */
    public static ListNode buildData() {
        ListNode node1 = new ListNode(1, null);
        ListNode node2 = new ListNode(2, node1);
        ListNode node3 = new ListNode(3, node2);
        ListNode node4 = new ListNode(4, node3);
        ListNode node5 = new ListNode(5, node4);
        return node5;
    }

    public static ListNode buildData3() {
        ListNode node1 = new ListNode(4, null);
        // ListNode node2 = new ListNode(3, node1);
        // ListNode node3 = new ListNode(3, node2);
        // ListNode node4 = new ListNode(2, node3);
        // ListNode node5 = new ListNode(1, node4);
        // ListNode node6 = new ListNode(1, node5);
        return node1;
    }

    public static ListNode buildData2() {
        ListNode node0 = new ListNode(6, null);
        ListNode node1 = new ListNode(6, node0);
        ListNode node2 = new ListNode(2, node1);
        ListNode node3 = new ListNode(3, node2);
        ListNode node4 = new ListNode(6, node3);
        ListNode node5 = new ListNode(4, node4);
        ListNode node6 = new ListNode(5, node5);
        ListNode node7 = new ListNode(6, node6);
        return node7;
    }

    /**
     * 打印链表
     * @param node
     */
    public static void printListNode(ListNode node){
        if(node == null) return ;
        StringBuilder str = new StringBuilder("[");
        int i = 0;
        while (node != null){
            if(i > 0){
                str.append(", ");
            }
            str.append(node.val);
            node = node.next;
            i++;
        }
        str.append("]");
        System.out.println(str);
    }

}
