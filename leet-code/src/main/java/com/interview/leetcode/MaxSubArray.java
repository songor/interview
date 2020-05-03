package com.interview.leetcode;

/**
 * 给定一个整数数组 nums，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
 * <p>
 * 示例：
 * 输入：[-2,1,-3,4,-1,2,1,-5,4],
 * 输出：6
 * 解释：连续子数组 [4,-1,2,1] 的和最大为 6。
 */
public class MaxSubArray {

    public static void main(String[] args) {
        int[] nums = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        System.out.println(dpOpt(nums));
    }

    /**
     * opt[i] 表示 nums 中以 nums[i] 结尾的最大子序和。
     * opt[0] = nums[0]
     * opt[i] = max(opt[i - 1] + nums[i], nums[i])
     *
     * @param nums
     * @return
     */
    private static int dpOpt(int[] nums) {
        int[] opt = new int[nums.length];
        opt[0] = nums[0];
        int max = nums[0];
        for (int i = 1; i < nums.length; i++) {
            opt[i] = Math.max(opt[i - 1] + nums[i], nums[i]);
            if (max < opt[i]) {
                max = opt[i];
            }
        }
        return max;
    }

}
