package com.spring.pseudocode.core.core.convert.converter;

//序列化接口类
public abstract interface Converter<S, T>
{
    public abstract T convert(S paramS);
}
