package com.interview.leetcode;

import java.util.LinkedList;
import java.util.List;

/**
 * 给定一个 没有重复 数字的序列，返回其所有可能的全排列。
 * <p>
 * 示例：
 * 输入：[1,2,3]
 * 输出：
 * [
 * [1,2,3],
 * [1,3,2],
 * [2,1,3],
 * [2,3,1],
 * [3,1,2],
 * [3,2,1]
 * ]
 */
public class Permute {

    public static void main(String[] args) {
        int[] nums = {1, 2, 3};
        List<List<Integer>> result = solution(nums);
        System.out.println(result);
    }

    private static List<List<Integer>> res = new LinkedList<>();

    private static List<List<Integer>> solution(int[] nums) {
        backTrace(nums, 0, new LinkedList<>());
        return res;
    }

    private static void backTrace(int[] nums, int depth, LinkedList<Integer> path) {
        if (depth == nums.length) {
            res.add(new LinkedList<>(path));
        }
        for (int i = 0; i < nums.length; i++) {
            if (path.contains(nums[i])) {
                continue;
            }
            path.add(nums[i]);
            backTrace(nums, depth + 1, path);
            path.removeLast();
        }
    }

}
