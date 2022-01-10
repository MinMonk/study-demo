package com.monk.lian_biao;

/**
 * https://leetcode-cn.com/problems/remove-duplicates-from-sorted-list/
 *
 * 83. 删除排序链表中的重复元素
 */
public class _83_ {

    public static void main(String[] args) {
        ListNode node = ListNode.buildData3();
        ListNode.printListNode(node);
        ListNode.printListNode(deleteDuplicates(node));
    }

    public static ListNode deleteDuplicates(ListNode head) {
        // 注意这里返回的最好是head节点,而不是null,因为这个链表可能就一个元素
        if(head == null || head.next == null) return head;

        ListNode currNode = head;
        while (currNode.next != null){
            if(currNode.val == currNode.next.val){
                currNode.next = currNode.next.next;
            }else{
                currNode = currNode.next;
            }
        }

        return head;
    }
}
