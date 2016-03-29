package com.spark.tuple;

public class Tuple3<E, T, K> extends Tuple {
    private E e;
    private T t;
    private K k;

    Tuple3 (E e, T t, K k) {
        this.e = e;
        this.t = t;
        this.k = k;
    }

    public Optional<E> _1 () {
    return Optional.of(e);
    }

    public Optional<T> _2 () {
    return Optional.of(t);
    }

    public Optional<K> _3 () {
        return Optional.of(k);
    }
}
