package com.interview.leetcode;

/**
 * 编写一个函数来查找字符串数组中的最长公共前缀。
 * 如果不存在公共前缀，返回空字符串 ""。
 * <p>
 * 示例 1：
 * 输入：["flower","flow","flight"]
 * 输出："fl"
 * <p>
 * 示例 2：
 * 输入：["dog","racecar","car"]
 * 输出：""
 * 解释：输入不存在公共前缀。
 */
public class LongestCommonPrefix {

    public static void main(String[] args) {
        String[] strs = {"flower", "flow", "flight"};
        String result = solution1(strs);
        System.out.println(result);

        result = solution2(strs);
        System.out.println(result);
    }

    private static String solution1(String[] strs) {
        if (strs == null || strs.length < 1) {
            return "";
        }
        if (strs.length == 1) {
            return strs[0];
        }
        int minLen = strs[0].length();
        for (int i = 0; i < strs.length; i++) {
            minLen = Math.min(strs[i].length(), minLen);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < minLen; i++) {
            char current = ' ';
            boolean equals = false;
            for (int j = 0; j < strs.length; j++) {
                if (current == ' ') {
                    current = strs[j].charAt(i);
                } else {
                    equals = current == strs[j].charAt(i);
                    if (!equals) {
                        break;
                    }
                }
            }
            if (equals) {
                sb.append(current);
            } else {
                break;
            }
        }
        return sb.toString();
    }

    /**
     * LCP(S1...Sn) = LCP(LCP(LCP(S1,S2),S3),...Sn)
     *
     * @param strs
     * @return
     */
    private static String solution2(String[] strs) {
        String prefix = strs[0];
        for (int i = 1; i < strs.length; i++) {
            while (strs[i].indexOf(prefix) != 0) {
                prefix = prefix.substring(0, prefix.length() - 1);
                if (prefix.length() == 0) {
                    return "";
                }
            }
        }
        return prefix;
    }

}
