package com.spark.tuple;

/**
 * 处理类
 */
public class Optional<X> {

    public X x;

    public Optional()
    {

    }

    public Optional(X x)
    {
        this.x=x;
    }

    public static <T> Optional of(T t)
    {
        return new Optional(t);
    }

    public static Optional  absent()
    {
        return new Optional();
    }

    public X get()
    {
        return x;
    }

}
