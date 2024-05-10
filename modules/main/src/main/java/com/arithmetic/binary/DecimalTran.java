package com.arithmetic.binary;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 十进制转二进制，十六进制
 */
public class DecimalTran {

    public static void main1(String [] args) {
        Scanner scanner = new Scanner(System.in);
        int dm = scanner.nextInt();
        int bk = dm;
        if(dm<=0) {
            System.out.println("0");
            return;
        }
        List<String> list = new ArrayList<String>();
        while(dm>0) {
            int m = dm%16;
            if(m<=9) {
                list.add(""+m);
            }else if(m==10) {
                list.add("A");
            }else if(m==11) {
                list.add("B");
            }else if(m==12) {
                list.add("C");
            }else if(m==13) {
                list.add("D");
            }else if(m==14) {
                list.add("E");
            }else if(m==15) {
                list.add("F");
            }
            dm = dm/16;
        }
        String rt ="";
        for(int i=list.size()-1;i>=0;i--) {
            rt+=list.get(i);
        }
        System.out.println(rt);
        System.out.println(Integer.toHexString(bk));
    }

    public static void main(String [] args) {
        Scanner scanner = new Scanner(System.in);
        int dm = scanner.nextInt();
        int bk = dm;
        if(dm<=0) {
            System.out.println("0");
            return;
        }
        List<String> list = new ArrayList<String>();
        while(dm>0) {
            int m = dm%2;
            if(m==0) {
                list.add("0");
            }else {
                list.add("1");
            }
            dm = dm/2;
        }
        String rt ="";
        for(int i=list.size()-1;i>=0;i--) {
            rt+=list.get(i);
        }
        System.out.println(rt);
        System.out.println(Integer.toBinaryString(bk));
    }

}
