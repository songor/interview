package com.interview.leetcode;

/**
 * 实现 pow(x, n)，即计算 x 的 n 次幂函数。
 */
public class Pow {

    public static void main(String[] args) {
        System.out.println(solution(2.00000, 10));
    }

    /**
     * 幂运算：
     * 3^100 = 3^50 * 3^50
     * 3^50 = 3^25 * 3^25
     * 3^25 = 3^12 * 3^12 * 3
     * 3^12 = 3^6 * 3^6
     * 3^6 = 3^3 * 3^3
     * 3^3 = 3 * 3 * 3
     *
     * @param x
     * @param n
     * @return
     */
    private static double solution(double x, int n) {
        long N = n;
        if (N < 0) {
            x = 1 / x;
            N = -N;
        }
        return fastPow(x, N);
    }

    private static double fastPow(double x, long n) {
        if (n == 0) {
            return 1;
        }
        double half = fastPow(x, n / 2);
        if (n % 2 == 0) {
            return half * half;
        } else {
            return half * half * x;
        }
    }

}
