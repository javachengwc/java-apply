package com.arithmetic.binary;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * ip与整数相互转换
 * ip地址的每段是一个0-255的整数，把每段拆分成一个二进制形式组合起来，然后把这个二进制数转变成一个长整数。
 * 举例：一个ip地址为192.168.10.220
 * 每段数字             相对应的二进制数
 * 192                   11000000
 * 168                   10101000
 * 10                    00001010
 * 220                   11011100
 * 组合起来即为：11000000 10101000 00001010 11011100,转换为10进制数就是：3232238300，即该IP地址转换后的数字
 */
public class IPAndDecimalTran {

    public static void main(String [] args) {
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNextLine()) {
            String str = scanner.nextLine();
            if(str.indexOf(".")>0) {
                String [] ay = str.split("\\.");
                String btr="";
                for(String per:ay) {
                    int pi = Integer.parseInt(per);
                    List<String> li = new ArrayList<String>();
                    while(pi>0) {
                        int m = pi%2;
                        if(m==0) {
                            li.add("0");
                        }else {
                            li.add("1");
                        }
                        pi = pi/2;
                    }
                    String tt ="";
                    for(int i=li.size()-1;i>=0;i--) {
                        tt+=li.get(i);
                    }
                    for(int i=0;i< 8-li.size();i++) {
                        tt ="0"+tt;
                    }
                    btr+=tt;
                }
                System.out.println(btr);
                long num = 0;
                for(int i=0;i<btr.length();i++) {
                    num = num*2;
                    num +=btr.charAt(i)-'0';
                }
                System.out.println(num);
                System.out.println(num ^4294967040L);
            }else {
                long data = Long.parseLong(str);
                List<String> li = new ArrayList<String>();
                while(data>0) {
                    long m = data%2;
                    if(m==0) {
                        li.add("0");
                    }else {
                        li.add("1");
                    }
                    data = data/2;
                }
                String tt ="";
                for(int i=li.size()-1;i>=0;i--) {
                    tt+=li.get(i);
                }
                for(int i=0;i< 32-li.size();i++) {
                    tt ="0"+tt;
                }
                System.out.println(tt);
                String[] sy = new String[4];
                for(int i=0;i<sy.length;i++) {
                    sy[i] = tt.substring(i*8,(i+1)*8);
                }
                String rr="";
                for(String per: sy) {
                    int num = 0;
                    for(int i=0;i<per.length();i++) {
                        num = num*2;
                        num +=per.charAt(i)-'0';
                    }
                    rr+=num+".";
                }
                rr=rr.substring(0,rr.length()-1);
                System.out.println(rr);
            }
        }
    }
}
