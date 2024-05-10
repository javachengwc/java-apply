package com.arithmetic.binary;

import java.util.Scanner;

/**
 * ip验证
 */
public class IPVerify {

    public static void main(String [] args) {
        Scanner scanner = new Scanner(System.in);
        String ip = scanner.nextLine();
        String[] ay = ip.split("\\.");
        if(ay.length!=4) {
            System.out.println("NO");
            return;
        }
        int maxNum = 0;
        for(int i=0;i<8;i++) {
           maxNum=maxNum*2;
           maxNum+=1;
        }
        int maxLen = new String(maxNum+"").length();
        //System.out.println(maxNum);
        for(int i=0;i<ay.length;i++) {
            String per = ay[i];
            if(per==null || per.equals("")) {
                System.out.println("NO");
                return;
            }
            int len = per.length();
            if(len>maxLen) {
                System.out.println("NO");
                return;
            }
            if(len>1 && per.charAt(0)-'0'<=0 ) {
                System.out.println("NO");
                return;
            }
            int di = Integer.parseInt(per);
            if(di<0 || di>maxNum) {
                System.out.println("NO");
                return;
            }
        }
        System.out.println("YES");
    }
}
