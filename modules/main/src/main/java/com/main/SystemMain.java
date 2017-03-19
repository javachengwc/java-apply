package com.main;

import java.io.File;

/**
 * 系统相关
 */
public class SystemMain {

    public static void main(String args [])
    {
        String path = System.getProperty("user.home");
        String sp = System.getProperty("path.separator");
        String fp =File.separator;
        System.out.println(path+" "+sp+" "+fp);

        System.out.println(Number.class.isAssignableFrom(Integer.class));
    }
}
