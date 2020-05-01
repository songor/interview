package com.interview.leetcode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 请你来实现一个 atoi 函数，使其能将字符串转换成整数。
 * 首先，该函数会根据需要丢弃无用的开头空格字符，直到寻找到第一个非空格的字符为止。接下来的转化规则如下：
 * 如果第一个非空字符为正或者负号时，则将该符号与之后面尽可能多的连续数字字符组合起来，形成一个有符号整数。
 * 假如第一个非空字符是数字，则直接将其与之后连续的数字字符组合起来，形成一个整数。
 * 该字符串在有效的整数部分之后也可能会存在多余的字符，那么这些字符可以被忽略，它们对函数不应该造成影响。
 * 注意：假如该字符串中的第一个非空格字符不是一个有效整数字符、字符串为空或字符串仅包含空白字符时，则你的函数不需要进行转换，即无法进行有效转换。
 * 在任何情况下，若函数不能进行有效的转换时，请返回 0。
 * <p>
 * 示例 1：
 * 输入："42"
 * 输出：42
 * <p>
 * 示例 2：
 * 输入："   -42"
 * 输出：-42
 * 解释：第一个非空白字符为 '-', 它是一个负号。我们尽可能将负号与后面所有连续出现的数字组合起来，最后得到 -42 。
 * <p>
 * 示例 3：
 * 输入："4193 with words"
 * 输出：4193
 * 解释：转换截止于数字 '3' ，因为它的下一个字符不为数字。
 * <p>
 * 示例 4：
 * 输入："words and 987"
 * 输出：0
 * 解释：第一个非空字符是 'w', 但它不是数字或正、负号，因此无法执行有效的转换。
 * <p>
 * 示例 5：
 * 输入："-91283472332"
 * 输出：-2147483648
 * 解释：数字 "-91283472332" 超过 32 位有符号整数范围，因此返回 INT_MIN。
 */
public class Atoi {

    public static void main(String[] args) {
        String str = "42";
        int result = solution1(str);
        System.out.println(result);
        result = solution2(str);
        System.out.println(result);

        str = "   -42";
        result = solution1(str);
        System.out.println(result);
        result = solution2(str);
        System.out.println(result);

        str = "4193 with words";
        result = solution1(str);
        System.out.println(result);
        result = solution2(str);
        System.out.println(result);

        str = "words and 987";
        result = solution1(str);
        System.out.println(result);
        result = solution2(str);
        System.out.println(result);

        str = "-91283472332";
        result = solution1(str);
        System.out.println(result);
        result = solution2(str);
        System.out.println(result);
    }

    private static int solution1(String str) {
        if (str == null || str.trim().length() == 0) {
            return 0;
        }
        str = str.trim();
        String pattern = "^[-+]?\\d+";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        if (m.find()) {
            String tmp = m.group();
            if (tmp.startsWith("+")) {
                tmp = tmp.substring(1);
            }
            try {
                return Integer.valueOf(tmp);
            } catch (NumberFormatException e) {
                if (tmp.startsWith("-")) {
                    return Integer.MIN_VALUE;
                } else {
                    return Integer.MAX_VALUE;
                }
            }
        } else {
            return 0;
        }
    }

    private static int solution2(String str) {
        if (str == null || str.trim().length() == 0) {
            return 0;
        }
        char[] arr = str.toCharArray();
        int i = 0;
        int flag = 0;
        int res = 0;
        while (arr[i] == ' ') {
            i++;
        }
        if (arr[i] == '-') {
            flag = 1;
        }
        if (arr[i] == '+' || arr[i] == '-') {
            i++;
        }
        while (i < arr.length && Character.isDigit(arr[i])) {
            int r = Character.getNumericValue(arr[i]);
            if (res > Integer.MAX_VALUE / 10 || (res == Integer.MAX_VALUE / 10 && r > 7)) {
                return flag == 0 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            }
            res = res * 10 + r;
            i++;
        }
        return flag == 0 ? res : -res;
    }

}