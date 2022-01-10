package com.monk.lian_biao;

/**
 * https://leetcode-cn.com/problems/middle-of-the-linked-list/
 *
 * 876. 链表的中间结点
 */
public class _876_ {

    public static void main(String[] args) {
        ListNode node = ListNode.buildData();
        ListNode.printListNode(node);
        System.out.println(middleNode(node).val);
    }

    /**
     * 借助快慢指针,慢指针走一步,快指针走两步,那么当快指针走到末尾的时候,慢指针正好在中间
     * @param head
     * @return
     */
    public static ListNode middleNode(ListNode head) {
        //定义快慢两个指针,都从head节点开始走
        ListNode slow = head, fast = head;

        // 当fast到达链表尾部时终止循环
        while (fast != null && fast.next != null){
            slow = slow.next;//满指针走一步
            fast = fast.next.next;//快指针走两步
        }

        return slow;
    }
}
