package com.mybatis.pseudocode.mybatis.cache;

import java.util.concurrent.locks.ReadWriteLock;

public abstract interface Cache
{
    public abstract String getId();

    public abstract void putObject(Object object1, Object object2);

    public abstract Object getObject(Object object);

    public abstract Object removeObject(Object object);

    public abstract void clear();

    public abstract int getSize();

    public abstract ReadWriteLock getReadWriteLock();
}