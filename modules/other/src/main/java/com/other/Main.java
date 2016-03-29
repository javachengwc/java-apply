package com.other;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * test
 */
public class Main {

    //limit ?,? 或LIMIT ?,? 结尾的正则表达式
    private static String limitStr="(limit|LIMIT)\\s+\\?\\s*,\\s*\\?\\s*$";

    //查询开头select ...from部分的正则表达式
    private static String queryStr="^(select|SELECT)(.*)(from|FROM)";

    public static void main(String args [])
    {
        String queryStr1="select * from users";
        String queryStr2 ="SELECT id,name from users limit ? , ? ";

        Pattern ptn1 = Pattern.compile(limitStr);
        Matcher m1 = ptn1.matcher(queryStr1);

        System.out.println(m1.find());

        Matcher m2 = ptn1.matcher(queryStr2);

        if(m2.find())
        {
            System.out.println(m2.group());
        }

        Pattern ptn2 = Pattern.compile(queryStr);
        Matcher m3 = ptn2.matcher(queryStr1);
        if(m3.find())
        {
            System.out.println(m3.group(2));
        }

        Matcher m4 = ptn2.matcher(queryStr2);
        if(m4.find())
        {
            System.out.println(m4.group(2));
        }

    }
}
