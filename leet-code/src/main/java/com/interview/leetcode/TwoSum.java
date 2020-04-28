package com.interview.leetcode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
 * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素不能使用两遍。
 * <p>
 * 示例：
 * 给定 nums = [2, 7, 11, 15], target = 9
 * 因为 nums[0] + nums[1] = 2 + 7 = 9
 * 所以返回 [0, 1]
 */
public class TwoSum {

    public static void main(String[] args) {
        int[] nums = {3, 2, 4};
        int target = 6;

        int[] result = solution1(nums, target);
        System.out.println(Arrays.toString(result));

        result = solution2(nums, target);
        System.out.println(Arrays.toString(result));
    }

    private static int[] solution1(int[] nums, int target) {
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    /**
     * 设置一个 map 容器 record 用来记录元素的值与索引，然后遍历数组 nums。
     * 每次遍历时使用临时变量 complement 用来保存目标值与当前值的差值
     * 在此次遍历中查找 record，查看是否有与 complement 一致的值，如果查找成功则返回查找值的索引值与当前变量的值 i
     * 如果未找到，则在 record 保存该元素与索引值 i
     *
     * @param nums
     * @param target
     * @return
     */
    private static int[] solution2(int[] nums, int target) {
        Map<Integer, Integer> record = new HashMap<>(nums.length * 4 / 3);
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (record.containsKey(complement)) {
                return new int[]{record.get(complement), i};
            }
            record.put(nums[i], i);
        }
        return null;
    }

}
