package com.monk.lian_biao;

/**
 * https://leetcode-cn.com/problems/reverse-linked-list/
 *
 * 206. 反转链表
 */
public class _206_ {

    public static void main(String[] args) {
        ListNode node = ListNode.buildData();
        ListNode.printListNode(node);

        ListNode.printListNode(reverseList(node));
        ListNode.printListNode(reverseList2(node));

    }


    /**
     * 使用递归实现反转
     * @param head
     * @return
     */
    public static ListNode reverseList(ListNode head) {
        if(head == null || head.next == null) return head;

        ListNode newHead = reverseList(head.next);
        head.next.next = head;
        head.next = null;
        return newHead;
    }

    /**
     *
     * 非递归实现反转
     * @param head
     * @return
     */
    public static ListNode reverseList2(ListNode head) {
        if(head == null || head.next == null) return head;

        ListNode newHead = null;
        while(head != null){
            ListNode temp = head.next;
            head.next = newHead;
            newHead = head;
            head = temp;
        }
        return newHead;
    }


}
