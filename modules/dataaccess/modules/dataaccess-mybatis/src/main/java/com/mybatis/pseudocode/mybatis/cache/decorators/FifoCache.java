package com.mybatis.pseudocode.mybatis.cache.decorators;

import com.mybatis.pseudocode.mybatis.cache.Cache;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.locks.ReadWriteLock;

//Cache的装饰器类，增强fifo功能
public class FifoCache  implements Cache
{
    //委派的cache对象
    private final Cache delegate;

    private final Deque<Object> keyList;

    private int size;

    public FifoCache(Cache delegate)
    {
        this.delegate = delegate;
        this.keyList = new LinkedList();
        this.size = 1024;
    }

    public String getId()
    {
        return this.delegate.getId();
    }

    public int getSize()
    {
        return this.delegate.getSize();
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void putObject(Object key, Object value)
    {
        cycleKeyList(key);
        this.delegate.putObject(key, value);
    }

    public Object getObject(Object key)
    {
        return this.delegate.getObject(key);
    }

    public Object removeObject(Object key)
    {
        return this.delegate.removeObject(key);
    }

    public void clear()
    {
        this.delegate.clear();
        this.keyList.clear();
    }

    public ReadWriteLock getReadWriteLock()
    {
        return null;
    }

    private void cycleKeyList(Object key) {
        this.keyList.addLast(key);
        if (this.keyList.size() > this.size) {
            Object oldestKey = this.keyList.removeFirst();
            this.delegate.removeObject(oldestKey);
        }
    }
}
