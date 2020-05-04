package com.interview.leetcode.exam;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Q2 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        System.out.println(restoreIpAddresses(str));
    }

    private static List<String> res;

    private static char[] arr;

    private static int length;

    public static List<String> restoreIpAddresses(String s) {
        res = new ArrayList<>();
        if (s.length() < 4) {
            return res;
        }
        arr = s.toCharArray();
        length = s.length();
        backTrace(new StringBuilder(), 0, -1, 3);
        return res;
    }

    /**
     * @param str   已拼凑的字符
     * @param index 第 index 个数字
     * @param num   每部分 8 位二进制数字对应的十进制数字
     * @param dot   剩余 "." 的个数
     */
    private static void backTrace(StringBuilder str, int index, int num, int dot) {
        if (num > 255) {
            return;
        }
        if (index == length) {
            if (dot == 0) {
                res.add(str.toString());
            }
            return;
        }
        /**
         * 选择 "."
         * 剩余可用的数字个数 length - index 的数量要小于等于 3 * dot
         * 比如，后续还有四个数字，当时剩余的点只有一个，那么后面的四个数字组成的数显然大于 255，就不合法。
         * 总的来说是因为，两个点之间最多 3 个数字
         */
        if (num != -1 && dot > 0 && (length - index) <= 3 * dot) {
            str.append(".");
            // num = -1 标识 "."
            backTrace(str, index, -1, dot - 1);
            str.deleteCharAt(str.length() - 1);
        }
        /**
         * 选择 arr[index]
         * 剩余可用的数字个数大于等于 "." 的个数。
         * 否则，还剩 2 个数字，还有 3 个点的话，一定会出现 .. 的非法组合
         */
        if (length - index >= dot && num != 0) {
            if (num == -1) {
                num = 0;
            }
            str.append(arr[index]);
            backTrace(str, index + 1, num * 10 + Character.getNumericValue(arr[index]), dot);
            str.deleteCharAt(str.length() - 1);
        }
    }

}
