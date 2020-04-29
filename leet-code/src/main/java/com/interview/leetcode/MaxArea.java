package com.interview.leetcode;

/**
 * 给你 n 个非负整数 a1，a2，...，an，每个数代表坐标中的一个点 (i, ai)。在坐标内画 n 条垂直线，垂直线 i 的两个端点分别为 (i, ai) 和 (i, 0)。
 * 找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
 * 说明：你不能倾斜容器，且 n 的值至少为 2。
 * <p>
 * 示例：
 * 输入：[1,8,6,2,5,4,8,3,7]
 * 输出：49
 */
public class MaxArea {

    public static void main(String[] args) {
        int[] height = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        int result = solution1(height);
        System.out.println(result);

        result = solution2(height);
        System.out.println(result);
    }

    private static int solution1(int[] height) {
        int max = 0;
        for (int i = 0; i < height.length; i++) {
            for (int j = i + 1; j < height.length; j++) {
                int tmp = Math.min(height[i], height[j]) * (j - i);
                if (tmp > max) {
                    max = tmp;
                }
            }
        }
        return max;
    }

    /**
     * 在初始时，左右指针分别指向数组的左右两端，它们可以容纳的水量为 min(1,7) * 8 = 8。
     * 此时我们需要移动一个指针。移动哪一个呢？直觉告诉我们，应该移动对应数字较小的那个指针（即此时的左指针）。
     * 这是因为，由于容纳的水量是由 两个指针指向的数字中较小值 * 指针之间的距离 决定的。
     * 如果我们移动数字较大的那个指针，那么前者 两个指针指向的数字中较小值 不会增加，后者 指针之间的距离 会减小，那么这个乘积会减小。
     * 因此，我们移动数字较大的那个指针是不合理的。
     * 因此，我们移动数字较小的那个指针。
     *
     * @param height
     * @return
     */
    private static int solution2(int[] height) {
        int max = 0;
        int i = 0, j = height.length - 1;
        while (i < j) {
            int tmp = Math.min(height[i], height[j]) * (j - i);
            max = Math.max(tmp, max);
            if (height[i] < height[j]) {
                i++;
            } else {
                j--;
            }
        }
        return max;
    }

}
