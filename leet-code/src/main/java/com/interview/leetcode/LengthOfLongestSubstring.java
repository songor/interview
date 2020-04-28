package com.interview.leetcode;

import java.util.HashSet;
import java.util.Set;

/**
 * 给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。
 * <p>
 * 示例 1：
 * 输入："abcabcbb"
 * 输出：3
 * 解释：因为无重复字符的最长子串是 "abc"，所以其长度为 3。
 * <p>
 * 示例 2：
 * 输入："bbbbb"
 * 输出：1
 * 解释：因为无重复字符的最长子串是 "b"，所以其长度为 1。
 * <p>
 * 示例 3：
 * 输入："pwwkew"
 * 输出：3
 * 解释：因为无重复字符的最长子串是 "wke"，所以其长度为 3。请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
 */
public class LengthOfLongestSubstring {

    public static void main(String[] args) {
        String str = "abcabcbb";
        int result = solution1(str);
        System.out.println(result);
        result = solution2(str);
        System.out.println(result);

        str = "bbbbb";
        result = solution1(str);
        System.out.println(result);
        result = solution2(str);
        System.out.println(result);

        str = "pwwkew";
        result = solution1(str);
        System.out.println(result);
        result = solution2(str);
        System.out.println(result);
    }

    private static int solution1(String str) {
        int max = 0;
        Set<Character> substring = new HashSet<>();
        int start = 0;
        while (start < str.length()) {
            int current = start;
            while (current < str.length()) {
                if (substring.contains(str.charAt(current))) {
                    if (substring.size() > max) {
                        max = substring.size();
                    }
                    substring.clear();
                    start++;
                    break;
                } else {
                    substring.add(str.charAt(current));
                    current++;
                }
            }
        }
        return max;
    }

    /**
     * 维护一个滑动窗口，窗口内的都是没有重复的字符，去尽可能的扩大窗口的大小，窗口不停的向右滑动。
     * （1）如果当前遍历到的字符从未出现过，那么直接扩大右边界；
     * （2）如果当前遍历到的字符出现过，则缩小窗口（左边索引向右移动），然后继续观察当前遍历到的字符；
     * （3）重复（1）（2），直到左边索引无法再移动；
     * （4）维护一个结果 res，每次用出现过的窗口大小来更新结果 res，最后返回 res 获取结果。
     *
     * @param str
     * @return
     */
    private static int solution2(String str) {
        int max = 0, i = 0, j = 0;
        int n = str.length();
        Set<Character> substring = new HashSet<>();
        while (i < n && j < n) {
            if (!substring.contains(str.charAt(j))) {
                substring.add(str.charAt(j++));
                max = Math.max(max, substring.size());
            } else {
                substring.remove(str.charAt(i++));
            }
        }
        return max;
    }

}
