package com.struct.set;

import java.util.HashSet;

public class SetTest {

    public static void main(String [] args ) {
        HashSet<Integer> hashSet = new HashSet<Integer>();
        hashSet.add(null);//HashSet允许元素为空
        hashSet.add(1);
        System.out.println(hashSet.size());
    }
}
