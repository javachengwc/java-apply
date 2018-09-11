package com.mybatis.pseudocode.mybatis.cache;


import com.mybatis.pseudocode.mybatis.cache.decorators.TransactionalCache;

import java.util.HashMap;
import java.util.Map;

//事务缓存管理器
public class TransactionalCacheManager
{
    private final Map<Cache, TransactionalCache> transactionalCaches = new HashMap();

    public void clear(Cache cache) {
        getTransactionalCache(cache).clear();
    }

    public Object getObject(Cache cache, CacheKey key) {
        return getTransactionalCache(cache).getObject(key);
    }

    public void putObject(Cache cache, CacheKey key, Object value) {
        getTransactionalCache(cache).putObject(key, value);
    }

    public void commit() {
        for (TransactionalCache txCache : this.transactionalCaches.values())
            txCache.commit();
    }

    public void rollback()
    {
        for (TransactionalCache txCache : this.transactionalCaches.values())
            txCache.rollback();
    }

    private TransactionalCache getTransactionalCache(Cache cache)
    {
        TransactionalCache txCache = (TransactionalCache)this.transactionalCaches.get(cache);
        if (txCache == null) {
            txCache = new TransactionalCache(cache);
            this.transactionalCaches.put(cache, txCache);
        }
        return txCache;
    }
}
