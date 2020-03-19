package com.cache.springredis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CounterService {

    private static String COUNT_CACHE_KEY="COUNT";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    //查询计数器值
    public Integer getCounterValue() {
        String key =COUNT_CACHE_KEY;
        Integer count = (Integer)redisTemplate.opsForValue().get(key);
        return count;
    }

    //改变值
    public Long changeCount(Long count) {
        String key =COUNT_CACHE_KEY;
        Long after = redisTemplate.opsForValue().increment(key, count);
        return  after;
    }
}
