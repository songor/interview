package com.interview.leetcode;

import java.util.LinkedList;

/**
 * 给出两个 非空 的链表用来表示两个非负的整数。其中，它们各自的位数是按照 逆序 的方式存储的，并且它们的每个节点只能存储 一位 数字。
 * 如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。
 * 您可以假设除了数字 0 之外，这两个数都不会以 0 开头。
 * <p>
 * 示例：
 * 输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
 * 输出：7 -> 0 -> 8
 * 原因：342 + 465 = 807
 */
public class AddTwoNumbers {

    public static void main(String[] args) {
        LinkedList<Integer> l1 = new LinkedList<>();
        l1.offerFirst(3);
        l1.offerFirst(4);
        l1.offerFirst(2);
        LinkedList<Integer> l2 = new LinkedList<>();
        l2.offerFirst(4);
        l2.offerFirst(6);
        l2.offerFirst(5);
        System.out.println("l1 -> " + l1);
        System.out.println("l2 -> " + l2);
        LinkedList<Integer> result = solution(l1, l2);
        System.out.println("result -> " + result);
    }

    /**
     * 设立一个表示进位的变量 carried，建立一个新链表，把输入的两个链表从头往后同时处理，每两个相加，将结果加上 carried 后的值作为一个新节点到新链表后面。
     *
     * @param l1
     * @param l2
     * @return
     */
    private static LinkedList<Integer> solution(LinkedList<Integer> l1, LinkedList<Integer> l2) {
        LinkedList<Integer> result = new LinkedList<>();
        int current;
        int carried = 0;
        while (l1.peekFirst() != null || l2.peekFirst() != null) {
            Integer a = l1.pollFirst();
            Integer b = l2.pollFirst();
            a = a == null ? 0 : a;
            b = b == null ? 0 : b;
            current = (a + b + carried) % 10;
            carried = (a + b + carried) / 10;
            result.add(current);
        }
        return result;
    }

}
