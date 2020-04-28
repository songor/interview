package com.interview.leetcode.exam;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * [编程] 2的整数次幂
 * <p>
 * 描述
 * 请使用递归的方式判断一个给定的整数是否为2的整数次幂
 * 提示：当一个数 n = 2^k （k为非负整数）时，我们说n是2的整数（k）次幂。比如 2、4、8、16都是2的整数次幂，但3、7、14就不是
 * <p>
 * 输入
 * 一行，一个正整数n
 * <p>
 * 输入约束：
 * 1<=n<=2^31
 * <p>
 * 输出
 * 一行，数字1或0
 * 如果输入为2的整数次幂，则输出1，否则输出0
 */
public class U1104p2 {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine().trim());
        int result = isPowerOfTwo(n);
        System.out.println(result);
    }

    private static int isPowerOfTwo(int n) {
        assert n >= 1 && n <= Integer.MAX_VALUE : "Invalid Input";
        int result;
        if (n == 1) {
            result = 1;
        } else if (n % 2 == 0) {
            n /= 2;
            return isPowerOfTwo(n);
        } else {
            result = 0;
        }
        return result;
    }

}
