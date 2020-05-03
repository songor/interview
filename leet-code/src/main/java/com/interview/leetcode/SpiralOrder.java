package com.interview.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * 给定一个包含 m * n 个元素的矩阵（m 行，n 列），请按照顺时针螺旋顺序，返回矩阵中的所有元素。
 * <p>
 * 示例 1：
 * 输入：
 * [
 * [ 1, 2, 3 ],
 * [ 4, 5, 6 ],
 * [ 7, 8, 9 ]
 * ]
 * 输出：[1,2,3,6,9,8,7,4,5]
 * <p>
 * 示例 2：
 * 输入：
 * [
 * [1, 2, 3, 4],
 * [5, 6, 7, 8],
 * [9,10,11,12]
 * ]
 * 输出：[1,2,3,4,8,12,11,10,9,5,6,7]
 */
public class SpiralOrder {

    public static void main(String[] args) {
        int[][] matrix = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12}};
        List<Integer> res = solution(matrix);
        System.out.println(res);
    }

    /**
     * 首先设定上下左右边界
     * 其次向右移动到最右，此时第一行因为已经使用过了，可以将其从图中删去，体现在代码中就是重新定义上边界
     * 判断若重新定义后，上下边界交错，表明螺旋矩阵遍历结束，跳出循环，返回答案
     * 若上下边界不交错，则遍历还未结束，接着向下向左向上移动，操作过程与第一，二步同理
     * 不断循环以上步骤，直到某两条边界交错，跳出循环，返回答案
     *
     * @param matrix
     * @return
     */
    private static List<Integer> solution(int[][] matrix) {
        if (matrix.length == 0) {
            return new ArrayList<>();
        }

        int left = 0;
        int right = matrix[0].length - 1;
        int top = 0;
        int bottom = matrix.length - 1;
        List<Integer> res = new ArrayList<>(matrix.length * matrix[0].length);
        int i;
        while (true) {
            i = left;
            while (i <= right) {
                res.add(matrix[top][i]);
                i++;
            }
            if (++top > bottom) {
                break;
            }
            i = top;
            while (i <= bottom) {
                res.add(matrix[i][right]);
                i++;
            }
            if (--right < left) {
                break;
            }
            i = right;
            while (i >= left) {
                res.add(matrix[bottom][i]);
                i--;
            }
            if (--bottom < top) {
                break;
            }
            i = bottom;
            while (i >= top) {
                res.add(matrix[i][left]);
                i--;
            }
            if (++left > right) {
                break;
            }
        }
        return res;
    }

}
