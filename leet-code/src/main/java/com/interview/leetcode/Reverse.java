package com.interview.leetcode;

import java.util.Arrays;

/**
 * 给出一个 32 位的有符号整数，你需要将这个整数中每位上的数字进行反转。
 * <p>
 * 示例 1：
 * 输入：123
 * 输出：321
 * <p>
 * 示例 2：
 * 输入：-123
 * 输出：-321
 * <p>
 * 示例 3:
 * 输入：120
 * 输出：21
 */
public class Reverse {

    public static void main(String[] args) {
        int x = 123;
        int result = solution1(x);
        System.out.println(result);
        result = solution2(x);
        System.out.println(result);

        x = -123;
        result = solution1(x);
        System.out.println(result);
        result = solution2(x);
        System.out.println(result);

        x = 120;
        result = solution1(x);
        System.out.println(result);
        result = solution2(x);
        System.out.println(result);
    }

    /**
     * 把整数变成字符串，再去反转这个字符串
     *
     * @param x
     * @return
     */
    private static int solution1(int x) {
        char[] source = Integer.valueOf(x).toString().toCharArray();
        if (source.length <= 1) {
            return x;
        }
        char[] target = new char[source.length];
        for (int i = 0; i < source.length; i++) {
            target[source.length - 1 - i] = source[i];
        }
        while (target[0] == '0') {
            target = Arrays.copyOfRange(target, 1, target.length);
        }
        if (target[target.length - 1] == '-') {
            target = Arrays.copyOfRange(target, 0, target.length - 1);
            char[] tmp = new char[target.length + 1];
            System.arraycopy(target, 0, tmp, 1, target.length);
            tmp[0] = '-';
            target = tmp;
        }
        int result;
        try {
            result = Integer.valueOf(String.valueOf(target));
        } catch (NumberFormatException e) {
            result = 0;
        }
        return result;
    }

    /**
     * 栈
     */

    /**
     * 实际上我们只要能拿到这个整数的末尾数字就可以了。
     * 以 12345 为例，先拿到 5，再拿到 4，之后是 3，2，1，我们按这样的顺序就可以反向拼接处一个数字了，也就能达到反转的效果。
     * 怎么拿末尾数字呢？好办，用取模运算就可以了。
     * 1、将 12345 % 10 得到 5，之后将 12345 / 10
     * 2、将 1234 % 10 得到 4，再将 1234 / 10
     * 3、将 123 % 10 得到 3，再将 123 / 10
     * 4、将 12 % 10 得到 2，再将 12 / 10
     * 5、将 1 % 10 得到1，再将1 / 10
     * <p>
     * 循环的判断条件应该是 while(x!=0)，无论正数还是负数，按照上面不断的 /10 这样的操作，最后都会变成 0，所以判断终止条件就是 !=0。
     * 有了取模和除法操作，对于像 12300 这样的数字，也可以完美的解决掉了。
     * <p>
     * 如果某个数字大于 214748364，那后面就不用再判断了，肯定溢出了。
     * 如果某个数字等于 214748364 呢，需要要跟最大数的末尾数字比较，如果这个数字比 7 还大，说明溢出了。
     * <p>
     * 如果某个数字小于 -214748364 说明溢出了
     * 如果某个数字等于 -214748364，还需要跟最小数的末尾比较，即看它是否小于 8
     *
     * @param x
     * @return
     */
    private static int solution2(int x) {
        int res = 0;
        while (x != 0) {
            int tmp = x % 10;
            if (res > 214748364 || (res == 214748364 && tmp > 7)) {
                return 0;
            }
            if (res < -214748364 || (res == -214748364 && tmp < -8)) {
                return 0;
            }
            res = res * 10 + tmp;
            x /= 10;
        }
        return res;
    }

}
