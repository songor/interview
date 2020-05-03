package com.interview.leetcode;


/**
 * 给定一个 n × n 的二维矩阵表示一个图像。
 * 将图像顺时针旋转 90 度。
 * <p>
 * 说明：
 * 你必须在原地旋转图像，这意味着你需要直接修改输入的二维矩阵。请不要使用另一个矩阵来旋转图像。
 * <p>
 * 示例 1：
 * 给定 matrix =
 * [
 * [1,2,3],
 * [4,5,6],
 * [7,8,9]
 * ],
 * 原地旋转输入矩阵，使其变为：
 * [
 * [7,4,1],
 * [8,5,2],
 * [9,6,3]
 * ]
 * <p>
 * 示例 2：
 * 给定 matrix =
 * [
 * [ 5, 1, 9,11],
 * [ 2, 4, 8,10],
 * [13, 3, 6, 7],
 * [15,14,12,16]
 * ],
 * 原地旋转输入矩阵，使其变为：
 * [
 * [15,13, 2, 5],
 * [14, 3, 4, 1],
 * [12, 6, 8, 9],
 * [16, 7,10,11]
 * ]
 */
public class Rotate {

    public static void main(String[] args) {
        int[][] nums = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        solution1(nums);
        solution2(nums);
    }

    /**
     * 规律解决
     * <p>
     * 输入：
     * 1 2 3
     * 4 5 6
     * 7 8 9
     * <p>
     * 通过交换 matrix[i][j] matrix[j][i] 得到:
     * 1 4 7
     * 2 5 8
     * 3 6 9
     * <p>
     * 最后将得到每组数组倒序排列即可:
     * 7 4 1
     * 8 5 2
     * 9 6 3
     *
     * @param matrix
     */
    private static void solution1(int[][] matrix) {
        int len = matrix.length;
        for (int i = 0; i < len; i++) {
            for (int j = i; j < len; j++) {
                int tmp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = tmp;
            }
        }
        for (int i = 0; i < len; i++) {
            reverse(matrix[i]);
        }
    }

    private static void reverse(int[] matrix) {
        for (int i = 0; i < matrix.length / 2; i++) {
            int tmp = matrix[i];
            matrix[i] = matrix[matrix.length - i - 1];
            matrix[matrix.length - i - 1] = tmp;
        }
    }

    /**
     * 第一轮对下列数字进行旋转。定点为 [0,0] 和 [3,3]
     * [
     * [5,1,9,11],
     * [2,   10],
     * [13,  7],
     * [15,14,12,16]
     * ]
     * <p>
     * 第二轮对下列数字进行旋转。定点为 [1,1] 和 [2,2]
     * [
     * [3, 6],
     * [14,12]
     * ]
     * <p>
     * 按照顺时针方向
     *
     * @param matrix
     */
    private static void solution2(int[][] matrix) {
        int rowStart = 0;
        int rowEnd = matrix.length - 1;
        int colStart = 0;
        int colEnd = matrix[0].length - 1;
        while (rowStart < rowEnd) {
            rotate(matrix, rowStart, rowEnd, colStart, colEnd);
            rowStart++;
            rowEnd--;
            colStart++;
            colEnd--;
        }
    }

    private static void rotate(int[][] matrix, int rowStart, int rowEnd, int colStart, int colEnd) {
        int time = rowEnd - rowStart;
        for (int i = 0; i < time; i++) {
            int tmp = matrix[rowStart][colStart + i];
            matrix[rowStart][colStart + i] = matrix[rowEnd - i][colStart];
            matrix[rowEnd - i][colStart] = matrix[rowEnd][colEnd - i];
            matrix[rowEnd][colEnd - i] = matrix[rowStart + i][colEnd];
            matrix[rowStart + i][colEnd] = tmp;
        }
    }

}
