package com.other.findconflict;

import org.apache.commons.lang3.StringUtils;

import java.net.URL;

/**
 * 排查某个类no such method之类异常
 * 排查重复包或包冲突 可能有用
 */
public class ClassFrom {

    public static String getClassFrom(Class clazz)
    {
        ClassLoader classloader =   clazz.getClassLoader();
        String name = clazz.getName();
        String wpath= name.replace(".","/");
        wpath+=".class";
        //类似"org/apache/poi/util/IOUtils.class"
        URL res = classloader.getResource(wpath);
        String apath = res.getPath();
        return apath;
    }

    public static void main(String [] args)
    {
        System.out.println(getClassFrom(StringUtils.class));
    }

}
