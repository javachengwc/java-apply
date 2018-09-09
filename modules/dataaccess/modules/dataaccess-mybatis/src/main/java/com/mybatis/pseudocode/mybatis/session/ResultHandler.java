package com.mybatis.pseudocode.mybatis.session;

public abstract interface ResultHandler<T>
{
    public abstract void handleResult(ResultContext<? extends T> resultContext);
}
