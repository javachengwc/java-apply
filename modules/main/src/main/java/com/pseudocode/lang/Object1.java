package com.pseudocode.lang;

public class Object1 {

    Object object;

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

//     //notify()唤醒当前对象阻塞队列里的任一线程。
//     //只有当执行唤醒工作的线程离开同步块，即释放锁之后，被唤醒线程才能去竞争锁。
//     //notify()，notifyAll方法必须与synchronized同步块或同步方法中使用。
//     //因wait()而导致阻塞的线程是放在阻塞队列中的，因锁竞争失败导致的阻塞是放在同步队列中的，
//     //notify()/notifyAll()实质上是把阻塞队列中的线程放到同步队列中去。
//     public final native void notify();
//
//     public final native void notifyAll();
//
//     //wait()方法释放当前对象锁，并进入阻塞队列。
//     //wait()方法必须与synchronized同步块或同步方法中使用，因为它依赖于当前的锁进行等待/通知操作。
//     //如果尝试在没有锁的情况下调用wait()，就会抛出IllegalMonitorStateException异常。
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

//    每个类可以实现readObject、writeObject方法实现自己的序列化策略，
//    即使是transient修饰的成员变量也可以手动调用ObjectOutputStream的writeInt等方法将这个成员变量序列化
//    private void writeObject(ObjectOutputStream s) throws java.io.IOException {
//        //ObjectOutputStream是实现序列化的关键类，它可以将一个对象转换成二进制流
//    }
//
//    private void readObject(ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
//        //以通过ObjectInputStream将二进制流还原成对象
//    }

}
