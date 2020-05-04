package com.interview.leetcode.exam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Q1 {

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        int n = sc.nextInt();

        String[] nums = str.split(",");
        List<Integer> arr = new ArrayList<>(nums.length);
        Arrays.stream(nums).forEach(num -> arr.add(Integer.valueOf(num)));
        arr.sort(Integer::compareTo);
        System.out.println(arr.get(arr.size() - n));
    }

}
