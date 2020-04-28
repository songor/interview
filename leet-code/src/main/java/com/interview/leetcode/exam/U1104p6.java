package com.interview.leetcode.exam;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;

/**
 * [编程] 斐波那契数列
 * <p>
 * 假设n为正整数，斐波那契数列定义为：
 * f(n) = 1, n < 3;
 * f(n) = f(n-1) + f(n-2), n>=3
 * <p>
 * 现在请你来计算f(n)的值，但是不需要给出精确值，只要结果的后六位即可
 * <p>
 * 输入：一行，包含一个正整数n，且0<n<1000
 * 输出：一行，f(n)的后6位（十进制，不足6位不补零）
 */
public class U1104p6 {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine().trim());
//        int result = fibonacci(n);
        BigInteger result = fibonacci(n);
        String str = result.toString();
        if (str.length() > 6) {
            str = str.substring(str.length() - 6);
        }
        System.out.println(str);
    }

//    private static int fibonacci(int n) {
//        assert n > 0 && n < 1000 : "Invalid Input";
//        if (n < 3) {
//            return 1;
//        } else {
//            return fibonacci(n - 1) + fibonacci(n - 2);
//        }
//    }

    private static BigInteger fibonacci(int n) {
        assert n > 0 && n < 1000 : "Invalid Input";
        if (n < 3) {
            return BigInteger.valueOf(1);
        }
        BigInteger first = BigInteger.valueOf(1);
        BigInteger second = BigInteger.valueOf(1);
        BigInteger result = BigInteger.valueOf(0);
        for (int i = 3; i <= n; i++) {
            result = first.add(second);
            first = second;
            second = result;
        }
        return result;
    }

}
