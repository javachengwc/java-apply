package com.main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMain {

    public static void main(String [] args) {
        Pattern pattern = Pattern.compile("java");
        String test1 = "java";
        String test2 = "java123";
        String test3 = "123java";

        Matcher matcher = pattern.matcher(test1);
        System.out.println(matcher.matches());//true
        matcher = pattern.matcher(test2);
        System.out.println(matcher.matches());//false
        System.out.println("-----1-----------");

        matcher = pattern.matcher(test2);
        System.out.println(matcher.lookingAt());//true
        matcher = pattern.matcher(test3);
        System.out.println(matcher.lookingAt());//false
        System.out.println("-----2-----------");

        matcher = pattern.matcher(test1);
        System.out.println(matcher.find());//true
        matcher = pattern.matcher(test2);
        System.out.println(matcher.find());//true
        matcher = pattern.matcher(test3);
        System.out.println(matcher.find(2));//true
        matcher = pattern.matcher(test3);
        System.out.println(matcher.find(5));//false
        System.out.println("-----3-----------");

        Pattern pattern2 = Pattern.compile("java");
        String test = "123java456";

        Matcher matcher2 = pattern2.matcher(test);
        matcher2.find();
        System.out.println(matcher2.start());//返回3
        System.out.println(matcher2.end());  //返回7
        System.out.println(matcher2.group());//返回java
        System.out.println("-----------------");

        Pattern pattern5 = Pattern.compile("(java)(py)");
        String test5 = "123javapy456";
        Matcher matcher5 = pattern5.matcher(test5);
        matcher5.find();
        System.out.println(matcher5.groupCount());//返回2

        System.out.println(matcher5.group(1));//返回第1组匹配到的字符串"java"，注意起始索引是1
        System.out.println(matcher5.start(1));//返回3，第1组起始索引
        System.out.println(matcher5.end(1));//返回7 第1组结束索引

        System.out.println(matcher5.group(2));//返回第2组匹配到的字符串"py"
        System.out.println(matcher5.start(2));//返回7，第2组起始索引
        System.out.println(matcher5.end(2));//返回9 第2组结束索引
        System.out.println("-----5-----------");

        Pattern p = Pattern.compile("cat");
        Matcher m = p.matcher("fcatfcatfcatf");
        StringBuffer buf = new StringBuffer();
        while(m.find()){
            System.out.println(m.groupCount());
            System.out.println(m.group());
            m.appendReplacement(buf,"1");
        }
        //buf=f1f1f1，cat被替换为1,并且将最后匹配到之前的子串都添加到buf中
        System.out.println(buf);
        //buf=f1f1f1f，追加末尾的未匹配到的剩余的字符串到buf中
        m.appendTail(buf);
        System.out.println(buf);

        Pattern p1 = Pattern.compile("cat");
        Matcher m1 = p1.matcher("fcatfcatfcatf");
        System.out.println(m1.replaceAll("1"));

    }
}
