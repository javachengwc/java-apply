package com.storefront.manage.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class RedisCache<K, V> implements Cache<K, V> {

    private Logger logger = LoggerFactory.getLogger(RedisCache.class);

    private RedisTemplate<K, V> cache;

    private String keyPrefix = "shiro_redis_session:";

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public RedisCache(RedisTemplate<K, V> cache) {
        if (cache == null) {
            throw new IllegalArgumentException("Cache argument cannot be null.");
        }
        this.cache = cache;
    }

    public RedisCache(RedisTemplate<K, V> cache, String prefix) {

        this(cache);
        this.keyPrefix = prefix;
    }

    private K getCacheKey(K key) {
        return (K) (this.keyPrefix + key);
    }

    @Override
    public V get(K key) throws CacheException {
        try {
            if (key == null) {
                return null;
            } else {
                V rawValue = cache.opsForValue().get(getCacheKey(key));
                return rawValue;
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }

    }

    @Override
    public V put(K key, V value) throws CacheException {
        try {
            cache.opsForValue().set(getCacheKey(key), value, 30, TimeUnit.MINUTES);
            return value;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public V remove(K key) throws CacheException {
        try {
            V previous = get(key);
            cache.delete(getCacheKey(key));
            return previous;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public void clear() throws CacheException {
        try {
            cache.delete(keys());
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public int size() {
        try {
            return keys().size();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public Set<K> keys() {
        try {
            Set<K> keys = cache.keys(getCacheKey((K) "*"));
            if (CollectionUtils.isEmpty(keys)) {
                return Collections.emptySet();
            }
            return keys;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public Collection<V> values() {
        try {
            Set<K> keys = keys();
            if (!CollectionUtils.isEmpty(keys)) {
                List<V> values = new ArrayList<>(keys.size());
                for (K key : keys) {
                    V value = get(key);
                    if (value != null) {
                        values.add(value);
                    }
                }
                return Collections.unmodifiableList(values);
            } else {
                return Collections.emptyList();
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

}
