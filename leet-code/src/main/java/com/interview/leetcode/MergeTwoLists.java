package com.interview.leetcode;

/**
 * 将两个升序链表合并为一个新的升序链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。
 * <p>
 * 示例：
 * 输入：1->2->4, 1->3->4
 * 输出：1->1->2->3->4->4
 */
public class MergeTwoLists {

    public static void main(String[] args) {
        ListNode l1 = new ListNode(1);
        l1.next = new ListNode(2);
        l1.next.next = new ListNode(4);
        ListNode l2 = new ListNode(1);
        l2.next = new ListNode(3);
        l2.next.next = new ListNode(4);

        ListNode result = solution(l1, l2);
        StringBuilder str = new StringBuilder();
        while (result != null) {
            str.append(result.val).append("->");
            result = result.next;
        }
        System.out.println(str.toString());
    }

    private static ListNode solution(ListNode l1, ListNode l2) {
        ListNode result = new ListNode(-1);
        ListNode index = result;
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                index.next = l1;
                l1 = l1.next;
            } else {
                index.next = l2;
                l2 = l2.next;
            }
            index = index.next;
        }
        index.next = l1 == null ? l2 : l1;
        return result.next;
    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

}
