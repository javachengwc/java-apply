package com.arithmetic.array;

import java.util.Scanner;

/**
 * 公共子串
 * 查找两个字符串a,b中的最长公共子串
 */
public class CommonSubstring {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String shortStr = scanner.nextLine();
        String longStr = scanner.nextLine();
        String temporary;
        if (shortStr.length() > longStr.length()) {
            temporary = longStr;
            longStr = shortStr;
            shortStr = temporary;
        }
        int count = 0;
        int index = 0;
        for (int i = 0; i < shortStr.length(); i++) {
            if (longStr.contains(shortStr.substring(i - count, i + 1))) {
                count++;
                index = i;
            }
        }
        System.out.println(shortStr.substring(index - count + 1, index + 1));
    }
}
