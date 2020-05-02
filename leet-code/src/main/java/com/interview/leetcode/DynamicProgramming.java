package com.interview.leetcode;

/**
 * 示例 1：
 * 计算数组中不相邻的数之和的最大值。
 * <p>
 * arr = [1, 2, 4, 1, 7, 8, 3]
 * <p>
 * 动态规划方程，选或不选：
 * OPT(i) = max(OPT(i - 2) + arr[i], OPT(i-1))
 * 递归出口：
 * OPT(0) = arr[0]
 * OPT(1) = max(arr[0], arr[1])
 */
public class DynamicProgramming {

    public static void main(String[] args) {
        int[] arr = {1, 2, 4, 1, 7, 8, 3};
        System.out.println(recOpt(arr, 6));
        System.out.println(dpOpt(arr));
    }

    /**
     * 递归
     * 产生重叠子问题，O(2^n)
     *
     * @param arr
     * @param i
     * @return
     */
    private static int recOpt(int[] arr, int i) {
        if (i == 0) {
            return arr[0];
        }
        if (i == 1) {
            return Math.max(arr[0], arr[1]);
        }
        return Math.max(recOpt(arr, i - 2) + arr[i], recOpt(arr, i - 1));
    }

    /**
     * 动态规划
     *
     * @param arr
     * @return
     */
    private static int dpOpt(int[] arr) {
        int[] opt = new int[arr.length];
        opt[0] = arr[0];
        opt[1] = Math.max(arr[0], arr[1]);
        for (int i = 2; i < arr.length; i++) {
            opt[i] = Math.max(opt[i - 2] + arr[i], opt[i - 1]);
        }
        return opt[arr.length - 1];
    }

}
