package com.concurrent.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeMain {

    private static Unsafe UNSAFE;
    static {
        try {
            //通过此方式在引用类加载器加载的类中是获取不到UNSAFE对象的
            UNSAFE = Unsafe.getUnsafe();
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            //throw new Error(ex);
        }
    }

    public static void main(String [] args) {
        Unsafe unsafe = getUnsafe();
        //地址大小 4--32位系统 8--64位系统
        System.out.println(unsafe.addressSize());
        //UNSAFE为空，报异常
        //System.out.println(UNSAFE.addressSize());
    }

    private static Unsafe getUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe)field.get(null);
        }catch (Exception e) {
            e.printStackTrace(System.out);
            return null;
        }
    }
}
