package com.arithmetic.array;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * 对输入的数字去重并排序
 */
public class NoDuplicateAndSort {

    public static void main(String [] args) {
        Scanner in = new Scanner(System.in);
        int cnt = in.nextInt();
        Set<Integer> s = new HashSet<Integer>();
        for(int i=0;i<cnt;i++) {
            s.add(in.nextInt());
        }
        int count = s.size();
        Integer [] ab =s.toArray(new Integer[count]);
        for(int i=0;i<ab.length;i++) {
            for(int j=0;j<ab.length-i-1;j++) {
                if(ab[j]>ab[j+1]) {
                    int ttt =ab[j];
                    ab[j]=ab[j+1];
                    ab[j+1]=ttt;
                }
            }
        }
        for(int cc:ab) {
            System.out.println(cc);
        }
    }
}
