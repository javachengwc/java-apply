package com.main;

import com.entity.Entity;

/**
 * 类如果未重写 hashCode 方法，默认会继承并调用 Object 类中 hashCode 方法
 * 它是根据对象在内存堆中的内存地址来计算得出的一个唯一的值
 * 即使对象内部属性值相同，两者hashcode值也不会相同
 * 哈希值的优势在于可以利用它来迅速的查找某个对象，
 * 但在一般情况下调用 Object 类中的 hashCode 方法是没有实际使用意义
 * 重写 hashCode 方法主要是为了为了产生以属性为根据的哈希值
 * hashCode 通过属性值经过一系列计算得到，但也极小可能存在不同对象计算出了哈希值相同的情况,此情况称作哈希碰撞。
 * 在散列表中，哈希值是判断两对象是否相同（根据属性）的第一道屏障
 *
 * Object类提供的equals()方法默认是用==来进行比较的,只有两个对象是同一个对象时,才能返回相等的结果
 * 但在实际中，Object 类中的 equals 方法是没有实际使用意义,
 * 一般两个同类型不同的对象它们的内容相同,就认为它们相等。所以通常都要重写equals()方法
 * 在散列表中，当产生哈希碰撞时，可以进一步利用equals来对比对象属性内容是否确实相同。
 * 如果不同，认为是不同对象；如果相同，认为是同一对象
 * hashCode 和 equals 只有在散列表（哈希表）中才相互发挥作用
 * 当hashCode不同时，两个对象肯定不同
 * 当hashCode相同时，equals未必为true
 * 当equals返回true时，两个对象hashCode一定相同
 * 将某一个类的对象存入散列表时((如HashMap、HashTable),hashCode和equals都必须重写
 */
public class HashCodeEqualsMain {

    public static void main(String [] args) {
        Entity a = new Entity(1,"a");
        Entity b = new Entity(1,"a");

        System.out.println("a.hashcode="+a.hashCode());
        System.out.println("b.hashcode="+b.hashCode());
        System.out.println("a.equals(b)="+a.equals(b));
    }
}
