package com.interview.leetcode.exam;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * [编程] 没有括号的算式
 * <p>
 * 有一个写在黑板上的加减法算式，运算符只包含加号、减号和括号，但其中的括号被清洁工擦去了。现在需要你写一个算法计算这个算式括号被擦去之前的可能的最小结果值
 * <p>
 * 输入：
 * 仅有一行，被擦去括号后的算式
 * <p>
 * 输入约束：
 * 算式最多有50个字符，且其中仅包含0-9和+、-
 * 算式的第一个字符一定是数字
 * 算式中不会连续出现两个运算符
 * 算式中每个整数最多有5位
 * <p>
 * 输出：
 * 一个整数：即括号被擦去之前，该算式可能的最小结果值
 * <p>
 * 举例1：
 * 输入：
 * 55-50+40
 * 输出：
 * -35
 * 解释：
 * 通过增加括号，该算式有两种可能的结果：55-50+40=45和55-(50+40)=-35
 * <p>
 * 举例2：
 * 输入：
 * 10+20+30+40
 * 输出：
 * 100
 * 解释：
 * 由于输入中没有减号，因此无论怎么加括号，结果也只能是100
 * <p>
 * 举例3：
 * 输入：
 * 00009-00009
 * 输出：
 * 0
 * 解释：注意算式中的整数可能有前导0
 */
public class U1104p5 {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = br.readLine().trim();
        System.out.println(min(str));
    }

    private static int min(String str) {
        int result = 0;
        List<Integer> bracket = new ArrayList<>();
        boolean transfer = false;
        str = str.replace("-", "+-");
        String[] arr = str.split("[+]");
        for (int i = 0; i < arr.length; i++) {
            String value = arr[i];
            if (value.startsWith("-")) {
                transfer = true;
                bracket.add(Integer.valueOf(value.substring(1)));
            } else if (transfer) {
                bracket.add(Integer.valueOf(value));
            } else {
                result += Integer.valueOf(value);
            }
        }
        if (!bracket.isEmpty()) {
            result -= bracket.stream().mapToInt(Integer::intValue).sum();
        }
        return result;
    }

}
