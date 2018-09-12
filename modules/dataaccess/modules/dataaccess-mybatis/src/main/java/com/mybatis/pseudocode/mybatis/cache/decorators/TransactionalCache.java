package com.mybatis.pseudocode.mybatis.cache.decorators;

import com.mybatis.pseudocode.mybatis.cache.Cache;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;

//带事务特性的缓存
public class TransactionalCache implements Cache
{
    private static final Log log = LogFactory.getLog(TransactionalCache.class);

    private final Cache delegate;

    private boolean clearOnCommit;

    //临时未提交的缓存
    private final Map<Object, Object> entriesToAddOnCommit;

    //获取缓存数据时,没有命中的key集合
    private final Set<Object> entriesMissedInCache;

    public TransactionalCache(Cache delegate)
    {
        this.delegate = delegate;
        this.clearOnCommit = false;
        this.entriesToAddOnCommit = new HashMap();
        this.entriesMissedInCache = new HashSet();
    }

    public String getId()
    {
        return this.delegate.getId();
    }

    public int getSize()
    {
        return this.delegate.getSize();
    }

    public Object getObject(Object key)
    {
        Object object = this.delegate.getObject(key);
        if (object == null) {
            this.entriesMissedInCache.add(key);
        }

        //如果设置了clearOnCommit=true,都不会从缓存获取都数据
        if (this.clearOnCommit) {
            return null;
        }
        return object;
    }

    public ReadWriteLock getReadWriteLock()
    {
        return null;
    }

    public void putObject(Object key, Object object)
    {
        this.entriesToAddOnCommit.put(key, object);
    }

    public Object removeObject(Object key)
    {
        return null;
    }

    public void clear()
    {
        this.clearOnCommit = true;
        this.entriesToAddOnCommit.clear();
    }

    public void commit() {
        if (this.clearOnCommit) {
            this.delegate.clear();
        }
        //提交的时候，把临时未提交的缓存放入正式缓存
        flushPendingEntries();
        reset();
    }

    public void rollback() {
        unlockMissedEntries();
        reset();
    }

    private void reset() {
        this.clearOnCommit = false;
        this.entriesToAddOnCommit.clear();
        this.entriesMissedInCache.clear();
    }

    private void flushPendingEntries() {
        //把临时未提交的缓存放入正式缓存
        for (Map.Entry entry : this.entriesToAddOnCommit.entrySet()) {
            this.delegate.putObject(entry.getKey(), entry.getValue());
        }
        for (Object entry : entriesMissedInCache) {
            if (!entriesToAddOnCommit.containsKey(entry)) {
                delegate.putObject(entry, null);
            }
        }
    }

    private void unlockMissedEntries()
    {
        for (Iterator localIterator = this.entriesMissedInCache.iterator(); localIterator.hasNext(); ) {
            Object entry = localIterator.next();
            try {
                this.delegate.removeObject(entry);
            } catch (Exception e) {
                log.warn("Unexpected exception while notifiying a rollback to the cache adapter.Consider upgrading your cache adapter to the latest version.  Cause: " + e);
            }
        }
    }
}
