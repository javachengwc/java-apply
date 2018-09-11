package com.mybatis.pseudocode.mybatis.cache.impl;

import com.mybatis.pseudocode.mybatis.cache.Cache;
import org.apache.ibatis.cache.CacheException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

//具体的Cache实现类，内存保存缓存数据
public class PerpetualCache implements Cache
{
    private final String id;

    private Map<Object, Object> cache = new HashMap();

    public PerpetualCache(String id) {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    public int getSize()
    {
        return this.cache.size();
    }

    public void putObject(Object key, Object value)
    {
        this.cache.put(key, value);
    }

    public Object getObject(Object key)
    {
        return this.cache.get(key);
    }

    public Object removeObject(Object key)
    {
        return this.cache.remove(key);
    }

    public void clear()
    {
        this.cache.clear();
    }

    public ReadWriteLock getReadWriteLock()
    {
        return null;
    }

    public boolean equals(Object o)
    {
        if (getId() == null) {
            throw new CacheException("Cache instances require an ID.");
        }
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cache)) {
            return false;
        }

        Cache otherCache = (Cache)o;
        return getId().equals(otherCache.getId());
    }

    public int hashCode()
    {
        if (getId() == null) {
            throw new CacheException("Cache instances require an ID.");
        }
        return getId().hashCode();
    }
}
