package com.interview.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * 数字 n 代表生成括号的对数，请你设计一个函数，用于能够生成所有可能的并且 有效的 括号组合。
 * <p>
 * 示例：
 * 输入：n = 3
 * 输出：[
 * "((()))",
 * "(()())",
 * "(())()",
 * "()(())",
 * "()()()"
 * ]
 */
public class GenerateParenthesis {

    public static void main(String[] args) {
        List<String> result = solution(1);
        System.out.println(result);
    }

    private static List<String> res = new ArrayList<>();

    private static List<String> solution(int n) {
        StringBuilder path = new StringBuilder();
        dfs(n, n, path);
        return res;
    }

    private static void dfs(int left, int right, StringBuilder path) {
        if (left == 0 && right == 0) {
            res.add(path.toString());
            return;
        }
        // 剪枝
        if (left > right) {
            return;
        }
        if (left > 0) {
            // 做选择
            path.append("(");
            // 进入下一层决策树
            dfs(left - 1, right, path);
            // 取消选择
            path.deleteCharAt(path.length() - 1);
        }
        if (right > 0) {
            // 做选择
            path.append(")");
            // 进入下一层决策树
            dfs(left, right - 1, path);
            // 取消选择
            path.deleteCharAt(path.length() - 1);
        }
    }

}
