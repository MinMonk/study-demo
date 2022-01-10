package com.monk.lian_biao;

/**
 * https://leetcode-cn.com/problems/delete-node-in-a-linked-list/
 *
 * 237. 删除链表中的节点
 */
public class _237_ {

    public static void main(String[] args) {
        ListNode node = ListNode.buildData();
        ListNode.printListNode(node);

        deleteNode(node.next.next);

        ListNode.printListNode(node);
    }

    public static void deleteNode(ListNode node) {
        node.val = node.next.val;
        node.next = node.next.next;
    }
}
