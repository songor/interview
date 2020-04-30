package com.interview.leetcode;

import java.util.*;

/**
 * 给你一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c，使得 a + b + c = 0？请你找出所有满足条件且不重复的三元组。
 * 注意：答案中不可以包含重复的三元组。
 * <p>
 * 示例：
 * 给定数组 nums = [-1, 0, 1, 2, -1, -4]
 * 满足要求的三元组集合为：
 * [
 * [-1, 0, 1],
 * [-1, -1, 2]
 * ]
 */
public class ThreeSum {

    public static void main(String[] args) {
        int[] nums = {-1, 0, 1, 2, -1, -4};
        List<List<Integer>> result = solution1(nums);
        System.out.println(result);

        result = solution2(nums);
        System.out.println(result);
    }

    private static List<List<Integer>> solution1(int[] nums) {
        Set<List<Integer>> res = new HashSet<>();
        Arrays.sort(nums);
        for (int i = 1; i < nums.length - 1; i++) {
            int left = 0, right = nums.length - 1;
            while (left < i && right > i) {
                int sum = nums[left] + nums[i] + nums[right];
                if (sum > 0) {
                    right--;
                } else if (sum < 0) {
                    left++;
                } else {
                    List<Integer> tmp = Arrays.asList(nums[left], nums[i], nums[right]);
                    res.add(tmp);
                    right--;
                    left++;
                }
            }
        }
        return new ArrayList<>(res);
    }

    /**
     * 首先对数组进行排序，排序后固定一个数 nums[i]，再使用左右指针指向 nums[i] 后面的两端，数字分别为 nums[L] 和 nums[R]，
     * 计算三个数的和 sum，判断是否满足为 0，满足则添加进结果集。
     * 如果 nums[i] 大于 0，则三数之和必然无法等于 0，结束循环
     * 如果 nums[i] == nums[i-1]，则说明该数字重复，会导致结果重复，所以应该跳过
     * 当 sum == 0 时，nums[L] == nums[L+1] 则会导致结果重复，应该跳过，L++
     * 当 sum == 0 时，nums[R] == nums[R-1] 则会导致结果重复，应该跳过，R--
     *
     * @param nums
     * @return
     */
    private static List<List<Integer>> solution2(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> res = new ArrayList<>();
        for (int k = 0; k < nums.length - 2; k++) {
            if (nums[k] > 0) break;
            if (k > 0 && nums[k] == nums[k - 1]) continue;
            int i = k + 1, j = nums.length - 1;
            while (i < j) {
                int sum = nums[k] + nums[i] + nums[j];
                if (sum < 0) {
                    while (i < j && nums[i] == nums[++i]) ;
                } else if (sum > 0) {
                    while (i < j && nums[j] == nums[--j]) ;
                } else {
                    res.add(new ArrayList<>(Arrays.asList(nums[k], nums[i], nums[j])));
                    while (i < j && nums[i] == nums[++i]) ;
                    while (i < j && nums[j] == nums[--j]) ;
                }
            }
        }
        return res;
    }

}
