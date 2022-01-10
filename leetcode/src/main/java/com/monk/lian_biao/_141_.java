package com.monk.lian_biao;

/**
 * https://leetcode-cn.com/problems/linked-list-cycle/
 *
 * 141. 环形链表
 */
public class _141_ {


    /**
     * 借助快慢指针来解决:一个节点移动的快,一个节点移动的慢,如果这个链表有环,就好
     * 比是两个人在操场上跑步,跑得快的肯定会追上跑的慢的
     *
     * @param head
     * @return
     */
    public boolean hasCycle(ListNode head) {
        if(head == null || head.next == null) return false;

        ListNode slow = head;
        ListNode fast = head.next;
        while(fast != null && fast.next != null){
            // 开始追逐,slow每次往前移动一步,fast节点每次移动两步
            slow = slow.next;
            fast = fast.next.next;

            // 当快慢指针相遇了,即表示有环
            if(slow == fast) return true;
        }

        return false;
    }
}
