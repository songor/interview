package com.interview.leetcode;

/**
 * 假设按照升序排序的数组在预先未知的某个点上进行了旋转。
 * （例如，数组 [0,1,2,4,5,6,7] 可能变为 [4,5,6,7,0,1,2]）。
 * 搜索一个给定的目标值，如果数组中存在这个目标值，则返回它的索引，否则返回 -1。
 * 你可以假设数组中不存在重复的元素。
 * 你的算法时间复杂度必须是 O(log n) 级别。
 * <p>
 * 示例 1：
 * 输入：nums = [4,5,6,7,0,1,2], target = 0
 * 输出：4
 * <p>
 * 示例 2：
 * 输入：nums = [4,5,6,7,0,1,2], target = 3
 * 输出：-1
 */
public class Search {

    public static void main(String[] args) {
        int[] nums = {4, 5, 6, 7, 0, 1, 2};
        System.out.println(solution(nums, 0));
    }

    /**
     * 可以发现的是，我们将数组从中间分开成左右两部分的时候，一定有一部分的数组是有序的。拿示例来看，
     * 我们从 6 这个位置分开以后数组变成了 [4, 5, 6] 和 [7, 0, 1, 2] 两个部分，其中左边 [4, 5, 6] 这个部分的数组是有序的，其他也是如此。
     * <p>
     * 这启示我们可以在常规二分搜索的时候查看当前 mid 为分割位置分割出来的两个部分 [l, mid] 和 [mid + 1, r] 哪个部分是有序的，
     * 并根据有序的那个部分确定我们该如何改变二分搜索的上下界，因为我们能够根据有序的那部分判断出 target 在不在这个部分：
     * <p>
     * 如果 [l, mid - 1] 是有序数组，且 target 的大小满足 [nums[l],nums[mid]]，则我们应该将搜索范围缩小至 [l, mid - 1]，否则在 [mid + 1, r] 中寻找。
     * 如果 [mid, r] 是有序数组，且 target 的大小满足 nums[mid+1],nums[r]]，则我们应该将搜索范围缩小至 [mid + 1, r]，否则在 [l, mid - 1] 中寻找。
     *
     * @param nums
     * @param target
     * @return
     */
    private static int solution(int[] nums, int target) {
        if (nums.length < 1) {
            return -1;
        }
        if (nums.length == 1) {
            return nums[0] == target ? 0 : -1;
        }
        return search(nums, 0, nums.length - 1, target);
    }

    private static int search(int[] nums, int left, int right, int target) {
        if (left > right) {
            return -1;
        }
        int index = left + (right - left) / 2;
        if (nums[index] == target) {
            return index;
        }
        if (nums[index + 1] <= nums[right]) {
            if (target >= nums[index + 1] && target <= nums[right]) {
                return search(nums, index + 1, right, target);
            } else {
                return search(nums, left, index - 1, target);
            }
        }
        if (nums[index] >= nums[left]) {
            if (target >= nums[left] && target <= nums[index]) {
                return search(nums, left, index - 1, target);
            } else {
                return search(nums, index + 1, right, target);
            }
        }
        return -1;
    }

}
