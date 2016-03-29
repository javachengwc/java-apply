package com.spark.tuple;

/**
 * 元组
 * 在非常接近Java语言的Scala里有元组的定义：
 * val t = (1, 3.14, "Fred")，就是一个不同类型的数据放到一个线性集合里，
 * 在Java里模拟出一个类似的结构
 */
public abstract class Tuple {

    public static  <E, T> Tuple of (E e, T t) {
        return new Tuple2(e, t);
    }

    public static  <E, T, K> Tuple of (E e, T t, K k) {
        return new Tuple3(e, t, k);
    }

    public abstract <E> Optional<E> _1 ();

    public abstract <T> Optional<T> _2 ();

    public abstract <K> Optional<K> _3 ();

    public static void main (String[] args) {

        Tuple tuple2 = Tuple.of("hello", 1);
        Tuple tuple3 = Tuple.of("hello", 1, "hi");

        System.out.println(tuple2._1().get() + "|" + tuple2._2().get());
        System.out.println(tuple3._1().get() + "|" + tuple3._2().get() + "|" + tuple3._3().get());

    }
}
