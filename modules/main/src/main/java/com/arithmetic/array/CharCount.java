package com.arithmetic.array;

import java.util.Scanner;

/**
 * 计算字符在字符串中出现的次数，忽略大小写
 */
public class CharCount {

    public static void main(String [] args) {
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        String cs = scanner.nextLine();
        char ch = cs.toLowerCase().charAt(0);
        char [] cy= str.toLowerCase().toCharArray();
        int cnt = 0;
        for(char p: cy) {
            if(p==ch) {
                cnt++;
            }
        }
        System.out.println(cnt);
    }
}
