package com.pseudocode.lang;

public class Object1 {

    private static native void registerNatives();

    static {
        registerNatives();
    }

//    public final native Class<?> getClass();

    //hashCode方法的实现依赖于jvm，不同的jvm有不同的实现。
    //openJDK定义hashCode的方法在src/share/vm/prims/jvm.h和src/share/vm/prims/jvm.cpp。
    //hash的最终函数 get_next_hash，此函数提供了6种生成hash值的方法。
    //openJDK默认的hashCode方法实现和对象内存地址无关，jdk6和7中，它是使用第一种方法随机生成的数字。jdk8中，它是第五种方法,基于线程状态的数字。
    //使用-XX:hashCode=4来修改默认的hash方法实现。
    public native int hashCode();

    //若两个对象equals返回true，则hashCode有必要也返回相同的int数
    //若两个对象hashCode返回相同int数，则equals不一定返回true。
    //若两个对象hashCode返回不同int数，则equals一定返回false。
    //若两个对象equals返回false，则hashCode不一定返回不同的int数,但为不相等的对象生成不同hashCode值可以提高哈希表的性能。
    //hashCode是为了提高在散列结构存储中查找的效率，在线性表中没有作用。
    public boolean equals(Object1 obj) {
        return (this == obj);
    }

    protected native Object clone() throws CloneNotSupportedException;

    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }

    //对象被垃圾回收时，调用它finalize()方法
    protected void finalize() throws Throwable {

    }

//     public final native void notify();
//
//     public final native void notifyAll();
//
//     public final native void wait(long timeout) throws InterruptedException;
//
//     public final void wait(long timeout, int nanos) throws InterruptedException {
//        if (timeout < 0) {
//            throw new IllegalArgumentException("timeout value is negative");
//        }
//        if (nanos < 0 || nanos > 999999) {
//            throw new IllegalArgumentException("nanosecond timeout value out of range");
//        }
//        if (nanos > 0) {
//            timeout++;
//        }
//        wait(timeout);
//    }
//
//    public final void wait() throws InterruptedException {
//        wait(0);
//    }


}
