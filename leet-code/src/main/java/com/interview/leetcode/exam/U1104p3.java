package com.interview.leetcode.exam;

/**
 * [编程] 行动序列
 * <p>
 * 假设你站在一个无限大的平面的某一点上，接下来你要按照收到的指令序列依次循环执行。每条指令可能是以下三种之一：
 * S：前进一步，R：向右转90度，L：向左转90度
 * 现在需要你写一个算法，判断对于给定的指令序列，是否存在“绕圈子”的现象
 * 所谓“绕圈子”是指：当你无限循环执行给定的指令序列后，存在一个有限的正整数R，使得你所有经过的点都在以初始点为圆心，以R步长度为半径的圆内。另外，我们假设，每一步的长度都是相同的
 * <p>
 * 输入：
 * 第一行为一个整数n
 * 之后一共有n行，每行为一个指令序列
 * <p>
 * 输入约束：
 * n位于区间[1,50]
 * 从第二行开始，每行字符串长度为1-50，且仅包含字母L, S, R
 * <p>
 * 输出：
 * 仅有一个单词。
 * 按照输入给定的顺序，从第一行开始，每行从第一个字符开始。如果给定的指令序列存在绕圈子的现象，则输出bounded，否则输出unbounded
 * <p>
 * 举例1：
 * 输入
 * 1
 * SRSL
 * 输出
 * unbounded
 * 解释：假设你初始状态向北，你的行动序列依次为前进，右转，前进，左转，此时你仍然向北，但位置已经向东北方挪动了。只要时间足够长，你会一直向东北方前进，所以你没有在绕圈子
 * <p>
 * 举例2：
 * 输入
 * 2
 * SSSS
 * R
 * 输出
 * bounded
 * 解释：你会一直绕着一个边长为4步的小正方形循环行进，所以你在绕圈子
 */
public class U1104p3 {

    public static void main(String[] args) throws Exception {

    }

}
