package com.monk.lian_biao;

/**
 * https://leetcode-cn.com/problems/remove-linked-list-elements/
 * <p>
 * 203. 移除链表元素
 */
public class _203_ {

    public static void main(String[] args) {
        ListNode node = ListNode.buildData2();
        ListNode.printListNode(node);
        ListNode.printListNode(removeElements(node, 6));

    }

    public static ListNode removeElements(ListNode head, int val) {
        // 头结点需要单独处理
        if (head.val == val) head = head.next;

        if (head == null) return null;

        ListNode currNode = head;
        while (currNode.next != null) {
            if (currNode.next.val == val) {
                currNode.next = currNode.next.next;
            } else {
                currNode = currNode.next;
            }
        }
        return head;
    }

    public static ListNode removeElements2(ListNode head, int val) {
        if (head == null) return null;

        // 在头部节点前再新增一个虚拟节点
        ListNode visualNode = new ListNode(0);
        visualNode.next = head;

        ListNode currNode = visualNode;
        while (currNode.next != null) {
            if (currNode.next.val == val) {
                currNode.next = currNode.next.next;
            } else {
                currNode = currNode.next;
            }
        }
        return visualNode.next;
    }
}
