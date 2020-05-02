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
 * <p>
 * 示例 2：
 * 是否存在这样一个组合，使得 s = 9。
 * arr = [3, 34, 4, 12, 5, 2]
 * <p>
 * 动态规划方程，选或不选：
 * 选：subset(arr, i - 1, s - arr[i])
 * 不选：subset(arr, i - 1, s)
 * <p>
 * 递归出口：
 * if (s == 0) return true;
 * if (i == 0) return arr[i] == s;
 * if (arr[i] > s) return subset(arr, i - 1, s);
 */
public class DynamicProgramming {

    public static void main(String[] args) {
        // 示例 1
        int[] arr1 = {1, 2, 4, 1, 7, 8, 3};
        System.out.println(recOpt(arr1, arr1.length - 1));
        System.out.println(dpOpt(arr1));
        // 示例 2
        int[] arr2 = {3, 34, 4, 12, 5, 2};
        System.out.println(recSubset(arr2, arr2.length - 1, 9));
        System.out.println(dpSubset(arr2, 9));
    }

    /**
     * 递归，示例 1
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
     * 动态规划，示例 1
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

    /**
     * 递归，示例 2
     *
     * @param arr
     * @param i
     * @param s
     * @return
     */
    private static boolean recSubset(int[] arr, int i, int s) {
        if (s == 0) {
            return true;
        }
        if (i == 0) {
            return arr[i] == s;
        }
        if (arr[i] > s) {
            return recSubset(arr, i - 1, s);
        }
        return recSubset(arr, i - 1, s) || recSubset(arr, i - 1, s - arr[i]);
    }

    /**
     * 动态规划，示例 2
     * <p>
     * i/s    0 1 2 3 4 5 6 7 8 9
     * (3)  0 F F F T F F F F F F
     * (34) 1 T
     * (4)  2 T
     * (12) 3 T
     * (5)  4 T
     * (2)  5 T                 subset(arr[5], 9)
     *
     * @param arr
     * @param S
     * @return
     */
    private static boolean dpSubset(int[] arr, int S) {
        boolean[][] subset = new boolean[arr.length][S + 1];
        for (int i = 0; i < arr.length; i++) {
            subset[i][0] = true;
        }
        for (int i = 0; i < S + 1; i++) {
            subset[0][i] = false;
        }
        subset[0][arr[0]] = true;
        for (int i = 1; i < arr.length; i++) {
            for (int s = 0; s < S + 1; s++) {
                if (arr[i] > s) {
                    subset[i][s] = subset[i - 1][s];
                } else {
                    subset[i][s] = subset[i - 1][s - arr[i]] || subset[i - 1][s];
                }
            }
        }
        return subset[subset.length - 1][subset[subset.length - 1].length - 1];
    }
}
