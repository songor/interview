package com.interview.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 给定一个仅包含数字 2-9 的字符串，返回所有它能表示的字母组合。
 * 给出数字到字母的映射如下（与电话按键相同）。注意 1 不对应任何字母。
 * <p>
 * 示例：
 * 输入："23"
 * 输出：["ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"].
 */
public class LetterCombinations {

    public static void main(String[] args) {
        String digits = "23";
        List<String> result = solution1(digits);
        System.out.println(result);

        result = solution2(digits);
        System.out.println(result);
    }

    private static List<String> solution1(String digits) {
        List<String> tmp = new ArrayList<>();
        List<String> res = new ArrayList<>();
        for (int i = 0; i < digits.length(); i++) {
            char number = digits.charAt(i);
            char[] letter = getLetterByNumber(number);
            if (res.size() == 0) {
                for (int j = 0; j < letter.length; j++) {
                    res.add(Character.toString(letter[j]));
                }
            } else {
                for (int j = 0; j < res.size(); j++) {
                    for (int k = 0; k < letter.length; k++) {
                        tmp.add(res.get(j) + letter[k]);
                    }
                }
                res.clear();
                res.addAll(tmp);
                tmp.clear();
            }
        }
        return res;
    }

    private static char[] getLetterByNumber(char number) {
        char[] res;
        switch (number) {
            case '2':
                res = new char[]{'a', 'b', 'c'};
                break;
            case '3':
                res = new char[]{'d', 'e', 'f'};
                break;
            case '4':
                res = new char[]{'g', 'h', 'i'};
                break;
            case '5':
                res = new char[]{'j', 'k', 'l'};
                break;
            case '6':
                res = new char[]{'m', 'n', 'o'};
                break;
            case '7':
                res = new char[]{'p', 'q', 'r', 's'};
                break;
            case '8':
                res = new char[]{'t', 'u', 'v'};
                break;
            case '9':
                res = new char[]{'w', 'x', 'y', 'z'};
                break;
            default:
                throw new AssertionError("Illegal parameter");
        }
        return res;
    }

    private static List<String> result = new ArrayList<>();

    private static List<String> solution2(String digits) {
        Map<Character, char[]> map = new HashMap<>(16);
        map.put('2', new char[]{'a', 'b', 'c'});
        map.put('3', new char[]{'d', 'e', 'f'});
        map.put('4', new char[]{'g', 'h', 'i'});
        map.put('5', new char[]{'j', 'k', 'l'});
        map.put('6', new char[]{'m', 'n', 'o'});
        map.put('7', new char[]{'p', 'q', 'r', 's'});
        map.put('8', new char[]{'t', 'u', 'v'});
        map.put('9', new char[]{'w', 'x', 'y', 'z'});

        combine(digits, 0, new StringBuilder(), map);
        return result;
    }

    private static void combine(String digits, int depth, StringBuilder str, Map<Character, char[]> map) {
        // 触发结束条件
        if (depth == digits.length()) {
            result.add(str.toString());
            return;
        }
        char temp = digits.charAt(depth);
        char[] arr = map.get(temp);
        for (int i = 0; i < arr.length; i++) {
            // 做选择
            str.append(arr[i]);
            // 进入下一层决策树
            combine(digits, depth + 1, str, map);
            // 取消选择
            str.deleteCharAt(str.length() - 1);
        }
    }

}
