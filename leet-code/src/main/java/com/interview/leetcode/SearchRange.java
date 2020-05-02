package com.interview.leetcode;

import java.util.Arrays;

/**
 * 给定一个按照升序排列的整数数组 nums，和一个目标值 target。找出给定目标值在数组中的开始位置和结束位置。
 * 你的算法时间复杂度必须是 O(log n) 级别。
 * 如果数组中不存在目标值，返回 [-1, -1]。
 * <p>
 * 示例 1：
 * 输入：nums = [5,7,7,8,8,10], target = 8
 * 输出：[3,4]
 * <p>
 * 示例 2：
 * 输入：nums = [5,7,7,8,8,10], target = 6
 * 输出：[-1,-1]
 */
public class SearchRange {

    public static void main(String[] args) {
        int[] nums = {5, 7, 7, 8, 8, 10};
        int target = 8;
        System.out.println(Arrays.toString(solution(nums, target)));
    }

    private static int[] solution(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int index = left + (right - left) / 2;
            if (target < nums[index]) {
                right = index - 1;
            } else if (target > nums[index]) {
                left = index + 1;
            } else {
                if (nums[index] == nums[left] && nums[index] == nums[right]) {
                    return new int[]{left, right};
                } else if (nums[index] == nums[left]) {
                    right--;
                } else if (nums[index] == nums[right]) {
                    left++;
                } else {
                    right--;
                    left++;
                }
            }
        }
        return new int[]{-1, -1};
    }

}
