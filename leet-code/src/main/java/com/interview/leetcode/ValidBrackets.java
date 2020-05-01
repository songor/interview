package com.interview.leetcode;

import java.util.*;

/**
 * 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串，判断字符串是否有效。
 * 有效字符串需满足：
 * 左括号必须用相同类型的右括号闭合。
 * 左括号必须以正确的顺序闭合。
 * 注意空字符串可被认为是有效字符串。
 * <p>
 * 示例 1：
 * 输入："()"
 * 输出：true
 * <p>
 * 示例 2：
 * 输入："()[]{}"
 * 输出：true
 * <p>
 * 示例 3:
 * 输入："(]"
 * 输出：false
 * <p>
 * 示例 4：
 * 输入："([)]"
 * 输出：false
 * <p>
 * 示例 5：
 * 输入："{[]}"
 * 输出：true
 */
public class ValidBrackets {

    public static void main(String[] args) {
        String s = "()";
        boolean result = solution1(s);
        System.out.println(result);
        result = solution2(s);
        System.out.println(result);

        s = "()[]{}";
        result = solution1(s);
        System.out.println(result);
        result = solution2(s);
        System.out.println(result);

        s = "(]";
        result = solution1(s);
        System.out.println(result);
        result = solution2(s);
        System.out.println(result);

        s = "([)]";
        result = solution1(s);
        System.out.println(result);
        result = solution2(s);
        System.out.println(result);

        s = "{[]}";
        result = solution1(s);
        System.out.println(result);
        result = solution2(s);
        System.out.println(result);
    }

    private static boolean solution1(String s) {
        if (s.isEmpty()) {
            return true;
        }
        Set<Character> leftBrackets = new HashSet<>(4);
        Set<Character> rightBrackets = new HashSet<>(4);
        leftBrackets.addAll(Arrays.asList('(', '[', '{'));
        rightBrackets.addAll(Arrays.asList(')', ']', '}'));
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (leftBrackets.contains(c)) {
                stack.push(c);
            } else if (rightBrackets.contains(c)) {
                if (')' == c) {
                    if (!isValid(stack, '(')) {
                        return false;
                    }
                } else if (']' == c) {
                    if (!isValid(stack, '[')) {
                        return false;
                    }
                } else if ('}' == c) {
                    if (!isValid(stack, '{')) {
                        return false;
                    }
                } else {
                    throw new AssertionError("Illegal parameter");
                }
            } else {
                throw new AssertionError("Illegal parameter");
            }
        }
        if (stack.size() > 0) {
            return false;
        }
        return true;
    }

    private static boolean isValid(Stack<Character> stack, char c) {
        try {
            return c == stack.pop();
        } catch (EmptyStackException e) {
            return false;
        }
    }

    private static boolean solution2(String s) {
        Map<Character, Character> map = new HashMap<>(4);
        map.put(')', '(');
        map.put(']', '[');
        map.put('}', '{');
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (map.containsKey(c)) {
                char top = stack.isEmpty() ? '#' : stack.pop();
                if (top != map.get(c)) {
                    return false;
                }
            } else {
                stack.push(c);
            }
        }
        return stack.isEmpty();
    }

}
