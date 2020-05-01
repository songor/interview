package com.interview.leetcode;

/**
 * 给定一个链表，删除链表的倒数第 n 个节点，并且返回链表的头结点。
 * <p>
 * 示例：
 * 给定一个链表：1->2->3->4->5，和 n = 2
 * 当删除了倒数第二个节点后，链表变为 1->2->3->5
 */
public class RemoveNthFromEnd {

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(4);
        head.next.next.next.next = new ListNode(5);

        head = new ListNode(1);
        ListNode result = solution(head, 1);
        StringBuilder str = new StringBuilder();
        while (result != null) {
            str.append(result.val);
            result = result.next;
        }
        System.out.println(str.toString());
    }

    private static ListNode solution(ListNode head, int n) {
        ListNode index = head;
        ListNode previous = null;
        int i = 0;
        while (index != null) {
            if (i == n) {
                previous = head;
            }
            if (i > n) {
                previous = previous.next;
            }
            i++;
            index = index.next;
        }
        if (previous == null) {
            head = head.next;
        } else {
            ListNode target = previous.next;
            previous.next = target.next;
            target.next = null;
        }
        return head;
    }

    static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }
}
