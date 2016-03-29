package com.other.classloader;

import java.net.URL;
import java.net.URLClassLoader;

public class UrlClassLoaderPrg {

    public static void main(String[] args) {

        String classPath = "loader.HelloImpl";// Jar中的所需要加载的类的类名

        String jarPath = "file:///D:/tmp/test.jar";// jar所在的文件的URL

        ClassLoader cl;
        try {
            // 从Jar文件得到一个Class加载器
            cl = new URLClassLoader(new URL[] { new URL(jarPath) });
            // 从加载器中加载Class
            Class< ?> c = cl.loadClass(classPath);
            // 从Class中实例出一个对象
            //HelloIface impl = (HelloIface) c.newInstance();
            // 调用Jar中的类方法
            //System.out.println(impl.hello());
            // System.out.println(impl.sayHi());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
