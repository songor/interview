package com.interview.leetcode;

import java.util.LinkedList;
import java.util.List;

/**
 * for 选择 in 选择列表:
 * # 做选择
 * 将该选择从选择列表移除
 * 路径.add(选择)
 * backtrack(路径, 选择列表)
 * # 撤销选择
 * 路径.remove(选择)
 * 将该选择再加入选择列表
 */
public class BackTrace {

    private static List<List<Integer>> res = new LinkedList<>();

    /**
     * 输入一组不重复的数字，返回它们的全排列
     *
     * @param nums
     * @return
     */
    private static List<List<Integer>> permute(int[] nums) {
        LinkedList<Integer> track = new LinkedList<>();
        backTrace(nums, track);
        return res;
    }

    /**
     * 路径：记录在 track 中
     * 选择列表：nums 中不存在于 track 的那些元素
     * 结束条件：nums 中的元素全都在 track 中出现
     *
     * @param nums
     * @param track
     */
    private static void backTrace(int[] nums, LinkedList<Integer> track) {
        // 触发结束条件
        if (track.size() == nums.length) {
            res.add(new LinkedList<>(track));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            // 排除不合法的选择
            if (track.contains(nums[i])) {
                continue;
            }
            // 做选择
            track.add(nums[i]);
            // 进入下一层决策树
            backTrace(nums, track);
            // 取消选择
            track.removeLast();
        }
    }

    public static void main(String[] args) {
        int[] nums = {1, 2, 3};
        permute(nums);
        System.out.println(res);
    }

}
