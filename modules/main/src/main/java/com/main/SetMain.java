package com.main;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class SetMain {

    public static void main(String args []) {
        chechNull();
    }

    //hashSet可存放空元素,TreeSet不可存放空元素
    public static  void chechNull() {
        Set<String> strSet = new HashSet<String>();
        //Set<String> strSet = new TreeSet<String>(); 抛异常
        strSet.add("a");
        strSet.add(null);
        strSet.add("b");
        String c=null;
        //strSet.add(c);
        System.out.println(strSet.size());
        System.out.println(strSet.contains(c));
    }
}
