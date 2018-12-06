package com.shop.book.manage.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RedisCacheManager implements CacheManager {

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheManager.class);

    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<>();

    @Resource(name="redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    private String keyPrefix = "shiro_redis_cache:";

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        Cache c = caches.get(name);
        if (c == null) {
            c = new RedisCache<>(redisTemplate, keyPrefix);
            caches.put(name, c);
        }
        return c;
    }

}
