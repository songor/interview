package com.interview.leetcode;

/**
 * 「外观数列」是一个整数序列，从数字 1 开始，序列中的每一项都是对前一项的描述。前五项如下：
 * 1.     1
 * 2.     11
 * 3.     21
 * 4.     1211
 * 5.     111221
 * <p>
 * 1 被读作 "one 1"（"一个一"），即 11。
 * 11 被读作 "two 1s"（"两个一"），即 21。
 * 21 被读作 "one 2", "one 1"（"一个二" ,"一个一"），即 1211。
 * <p>
 * 给定一个正整数 n（1 ≤ n ≤ 30），输出外观数列的第 n 项。
 * <p>
 * 注意：整数序列中的每一项将表示为一个字符串。
 * <p>
 * 示例 1：
 * 输入：1
 * 输出："1"
 * 解释：这是一个基本样例。
 * <p>
 * 示例 2：
 * 输入：4
 * 输出："1211"
 * 解释：当 n = 3 时，序列是 "21"，其中我们有 "2" 和 "1" 两组，"2" 可以读作 "12"，也就是出现频次 = 1 而值 = 2；
 * 类似 "1" 可以读作 "11"。所以答案是 "12" 和 "11" 组合在一起，也就是 "1211"。
 */
public class CountAndSay {

    public static void main(String[] args) {
        System.out.println(solution(5));
    }

    private static String solution(int n) {
        String s = "1";
        int curr = 1;
        return rec(s, curr, n);
    }

    private static String rec(String s, int curr, int n) {
        if (curr == n) {
            return s;
        }
        StringBuilder str = new StringBuilder();
        int i = 0;
        int start = 0;
        while (i < s.length()) {
            if (s.charAt(i) == s.charAt(start)) {
                i++;
                if (i == s.length()) {
                    int count = i - start;
                    str.append(count).append(s.charAt(start));
                }
            } else {
                int count = i - start;
                str.append(count).append(s.charAt(start));
                start = i;
            }
        }
        return rec(str.toString(), curr + 1, n);
    }

}
