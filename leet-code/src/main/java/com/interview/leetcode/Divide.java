package com.interview.leetcode;

/**
 * 给定两个整数，被除数 dividend 和除数 divisor，将两数相除，要求不使用乘法、除法和 mod 运算符。
 * 返回被除数 dividend 除以除数 divisor 得到的商。
 * 整数除法的结果应当截去（truncate）其小数部分，例如：truncate(8.345) = 8 以及 truncate(-2.7335) = -2
 * 示例 1：
 * 输入：dividend = 10, divisor = 3
 * 输出：3
 * 解释：10/3 = truncate(3.33333..) = truncate(3) = 3
 * <p>
 * 示例 2：
 * 输入：dividend = 7, divisor = -3
 * 输出：-2
 * 解释：7/-3 = truncate(-2.33333..) = -2
 */
public class Divide {

    public static void main(String[] args) {
        System.out.println(solution(2147483647, 2));
    }

    private static int solution(int dividend, int divisor) {
        if (dividend == 0) {
            return 0;
        }
        if (dividend == Integer.MIN_VALUE && divisor == -1) {
            return Integer.MAX_VALUE;
        }
        boolean positive = true;
        if (dividend > 0) {
            dividend = -dividend;
            positive = !positive;
        }
        if (divisor > 0) {
            divisor = -divisor;
            positive = !positive;
        }
        if (dividend > divisor) {
            return 0;
        }
        int loop = 1;
        int tmp = divisor;
        while (dividend - tmp <= divisor) {
            dividend = dividend - tmp;
            loop += loop;
            tmp += tmp;
        }
        tmp = divisor;
        while (dividend - tmp <= divisor) {
            loop++;
            tmp += divisor;
        }
        return positive ? loop : -loop;
    }

}
