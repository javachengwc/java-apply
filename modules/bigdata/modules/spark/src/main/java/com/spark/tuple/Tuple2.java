package com.spark.tuple;

public class Tuple2<E, T> extends Tuple {

    private E e;

    private T t;

    Tuple2 (E e, T t) {
        this.e = e;
        this.t = t;
    }

    @Override
    public Optional<E> _1 () {
        return Optional.of(e);
    }

    @Override
    public Optional<T> _2 () {
        return Optional.of(t);
    }

    @Override
    public <E> Optional<E> _3() {
        return Optional.absent();
    }
}
