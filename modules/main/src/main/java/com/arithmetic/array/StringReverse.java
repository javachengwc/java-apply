package com.arithmetic.array;

import java.util.Scanner;

/**
 * 字符串反转
 */
public class StringReverse {

    public static void main(String [] args) {
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        char [] ch = str.toCharArray();
        int len = ch.length;
        int mid = len/2;
        for(int i=0;i<mid;i++) {
            char tmp = ch[i];
            ch[i] =ch[len-1-i];
            ch[len-1-i] = tmp;
        }
        String rt = new String(ch);
        System.out.println(rt);
    }
}
