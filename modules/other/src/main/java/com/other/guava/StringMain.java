package com.other.guava;

import com.google.common.base.Strings;

/**
 * 参考:http://outofmemory.cn/java/guava/
 */
public class StringMain {

    public static void main(String args [])
    {
        //Strings.isNullOrEmpty(input) demo
        String input = "";
        boolean isNullOrEmpty = Strings.isNullOrEmpty(input);
        System.out.println("input " + (isNullOrEmpty?"is":"is not") + " null or empty.");

        //Strings.commonPrefix(a,b) demo
        String a = "com.jd.coo.Hello";
        String b = "com.jd.coo.Hi";
        String ourCommonPrefix = Strings.commonPrefix(a, b);
        System.out.println("a,b common prefix is " + ourCommonPrefix);

        //Strings.commonSuffix(a,b) demo
        String c = "com.google.Hello";
        String d = "com.jd.Hello";
        String ourSuffix = Strings.commonSuffix(c,d);
        System.out.println("c,d common suffix is " + ourSuffix);
    }

}
