package com.mybatis.pseudocode.mybatis.session;

public abstract interface ResultContext<T>
{
    public abstract T getResultObject();

    public abstract int getResultCount();

    public abstract boolean isStopped();

    public abstract void stop();
}
