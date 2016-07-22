package com.other.guava;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
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

        Optional<String> kaka= Optional.fromNullable("kaka");
        System.out.println(kaka.isPresent());
        System.out.println(kaka.get().toString());

        Optional<String> kaka2= Optional.of("kaka2");
        System.out.println(kaka2.get().toString());

        Optional<String> nullStr= Optional.absent(); //空值
        //nullStr=Optional.fromNullable(null);
        System.out.println(nullStr.isPresent());

        String value="what";
        //字符串拼接，on("?")是join中各字串的连接符号
        String joinStr =Joiner.on("").join("[Token(", value, ")]");
        System.out.println(joinStr);

    }

}
