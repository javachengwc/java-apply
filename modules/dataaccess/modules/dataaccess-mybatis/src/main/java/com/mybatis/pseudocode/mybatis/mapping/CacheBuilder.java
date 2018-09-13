package com.mybatis.pseudocode.mybatis.mapping;

import com.mybatis.pseudocode.mybatis.cache.Cache;
import com.mybatis.pseudocode.mybatis.cache.decorators.LruCache;
import com.mybatis.pseudocode.mybatis.cache.impl.PerpetualCache;
import org.apache.ibatis.builder.InitializingObject;
import org.apache.ibatis.cache.CacheException;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CacheBuilder {
    private final String id;
    private Class<? extends Cache> implementation;
    private final List<Class<? extends Cache>> decorators;
    private Integer size;
    private Long clearInterval;
    private boolean readWrite;
    private Properties properties;
    private boolean blocking;

    public CacheBuilder(String id) {
        this.id = id;
        this.decorators = new ArrayList();
    }

    public CacheBuilder implementation(Class<? extends Cache> implementation) {
        this.implementation = implementation;
        return this;
    }

    public CacheBuilder addDecorator(Class<? extends Cache> decorator) {
        if (decorator != null) {
            this.decorators.add(decorator);
        }
        return this;
    }

    public CacheBuilder size(Integer size) {
        this.size = size;
        return this;
    }

    public CacheBuilder clearInterval(Long clearInterval) {
        this.clearInterval = clearInterval;
        return this;
    }

    public CacheBuilder readWrite(boolean readWrite) {
        this.readWrite = readWrite;
        return this;
    }

    public CacheBuilder blocking(boolean blocking) {
        this.blocking = blocking;
        return this;
    }

    public CacheBuilder properties(Properties properties) {
        this.properties = properties;
        return this;
    }

    public Cache build() {
        setDefaultImplementations();
        Cache cache = newBaseCacheInstance(this.implementation, this.id);
        setCacheProperties(cache);

        if (PerpetualCache.class.equals(cache.getClass())) {
            for (Class decorator : this.decorators) {
                cache = newCacheDecoratorInstance(decorator, cache);
                setCacheProperties(cache);
            }
            cache = setStandardDecorators(cache);
        }
//        } else if (!LoggingCache.class.isAssignableFrom(cache.getClass())) {
//            cache = new LoggingCache(cache);
//        }
        return cache;
    }

    private void setDefaultImplementations() {
        if (this.implementation == null) {
            this.implementation = PerpetualCache.class;
            if (this.decorators.isEmpty())
                this.decorators.add(LruCache.class);
        }
    }

    private void setCacheProperties(Cache cache)
    {
        //...
    }

    private Cache setStandardDecorators(Cache cache)
    {
        //...
        return cache;
    }

    private Cache newBaseCacheInstance(Class<? extends Cache> cacheClass, String id)
    {
        Constructor cacheConstructor = getBaseCacheConstructor(cacheClass);
        try {
            return (Cache)cacheConstructor.newInstance(new Object[] { id });
        } catch (Exception e) {
            throw new CacheException("Could not instantiate cache implementation (" + cacheClass + "). Cause: " + e, e);
        }

    }

    private Constructor<? extends Cache> getBaseCacheConstructor(Class<? extends Cache> cacheClass)
    {
        try {
            return cacheClass.getConstructor(new Class[] { String.class });
        } catch (Exception e) {
            throw new CacheException("Invalid base cache implementation (" + cacheClass + "). " +
                    " Base cache implementations must have a constructor that takes a String id as a parameter.  Cause: " + e, e);
        }
    }


    private Cache newCacheDecoratorInstance(Class<? extends Cache> cacheClass, Cache base)
    {
        Constructor cacheConstructor = getCacheDecoratorConstructor(cacheClass);
        try {
            return (Cache)cacheConstructor.newInstance(new Object[] { base });
        } catch (Exception e) {
            throw new CacheException("Could not instantiate cache decorator (" + cacheClass + "). Cause: " + e, e);
        }

    }

    private Constructor<? extends Cache> getCacheDecoratorConstructor(Class<? extends Cache> cacheClass)
    {
        try {
            return cacheClass.getConstructor(new Class[] { Cache.class });
        } catch (Exception e) {
            throw new CacheException("Invalid cache decorator (" + cacheClass + ").  " +
                    "Cache decorators must have a constructor that takes a Cache instance as a parameter.  Cause: " + e, e);
        }
    }
}
