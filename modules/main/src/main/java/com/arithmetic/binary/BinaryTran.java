package com.arithmetic.binary;

import java.util.Scanner;

/**
 * 二进制转成十进制
 */
public class BinaryTran {

    public static void main(String [] args) {
        Scanner scanner= new Scanner(System.in);
        while (scanner.hasNext()) {
            String str = scanner.next();
            int len = str.length();
            int sum=0;
            for(int i=0;i<len;i++) {
                char cur = str.charAt(i);
                int d = cur-'0';
                sum =sum*2;
                sum+=d;
            }
            System.out.println(sum);
        }
    }
}
