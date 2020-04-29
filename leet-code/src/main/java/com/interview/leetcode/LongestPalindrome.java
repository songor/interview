package com.interview.leetcode;

/**
 * 给定一个字符串 s，找到 s 中最长的回文子串。你可以假设 s 的最大长度为 1000。
 * <p>
 * 示例 1：
 * 输入："babad"
 * 输出："bab"
 * 注意："aba" 也是一个有效答案。
 * <p>
 * 示例 2：
 * 输入："cbbd"
 * 输出："bb"
 */
public class LongestPalindrome {

    public static void main(String[] args) {
        String s = "babad";
        String result = solution(s);
        System.out.println(result);

        s = "cbbd";
        result = solution(s);
        System.out.println(result);
    }

    /**
     * 从每一个位置出发，向两边扩散即可，遇到不是回文的时候结束。
     * 举个例子，str = acdbbdaa 我们需要寻找从第一个 b（位置为 3）出发最长回文串为多少。怎么寻找？
     * 首先往左寻找与当期位置相同的字符，直到遇到不相等为止。
     * 然后往右寻找与当期位置相同的字符，直到遇到不相等为止。
     * 最后左右双向扩散，直到左和右不相等。
     * 每个位置向两边扩散都会出现一个窗口大小（len）。如果 len > maxLen（用来表示最长回文串的长度）。则更新 maxLen 的值。
     * 因为我们最后要返回的是具体子串，而不是长度，因此，还需要记录一下 maxLen 时的起始位置（maxStart），即此时还要 maxStart=left。
     *
     * @param s
     * @return
     */
    private static String solution(String s) {
        if (s == null || s.length() <= 0) {
            return "";
        }
        int maxLen = 0;
        int maxStart = 0;
        for (int i = 0; i < s.length(); i++) {
            int len = 1;
            int left = i - 1;
            while (left >= 0 && s.charAt(left) == s.charAt(i)) {
                left--;
                len++;
            }
            int right = i + 1;
            while (right < s.length() && s.charAt(right) == s.charAt(i)) {
                right++;
                len++;
            }
            while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
                left--;
                right++;
                len += 2;
            }
            if (len > maxLen) {
                maxLen = len;
                maxStart = left;
            }
        }
        return s.substring(maxStart + 1, maxStart + 1 + maxLen);
    }

}
