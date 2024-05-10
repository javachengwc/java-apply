package com.arithmetic.binary;

import java.util.Scanner;

/**
 * 十六进制转十进制
 */
public class HexadecimalTran {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while(in.hasNext()){
            String hex = in.next();
            int len = hex.length();
            int sum = 0,m=0;
            for(int i=2;i<len;i++){
                char temp = hex.charAt(i);
                if(temp>='A'){
                    m = temp - 'A' +10;
                }else if(temp>='0' &&temp<='9'){
                    m = temp -'0';
                }
                sum=sum*16;
                sum+=m;
            }
            System.out.println(sum);
        }
    }

    public static void main2(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            //0xEF
            String str = in.nextLine();
            str = str.substring(2);
            char [] cs = str.toLowerCase().toCharArray();
            int d=0;
            int i=cs.length-1;
            String jy = "0123456789abcdef";
            for(char p:cs) {
                int c=0;
                for(int j=0;j<jy.length();j++) {
                    if(jy.charAt(j)==p) {
                        c=j;
                        break;
                    }
                }
                d +=c*Math.pow(16,i);
                i--;
            }
            System.out.println(d);
        }
    }
}
