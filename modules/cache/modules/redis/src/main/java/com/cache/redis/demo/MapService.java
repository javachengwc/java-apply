package com.cache.redis.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * redis操作map,hashmap类
 */
@Service
public class MapService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    public void setData(String key,Map<String,String> data)
    {
        HashOperations operations = redisTemplate.opsForHash();
        for(String perKey:data.keySet())
        {
            operations.put(key,perKey,data.get(perKey));
        }
        redisTemplate.expire(key,10, TimeUnit.MINUTES);

    }

    public Map<String,String> getData(String key)
    {
        HashOperations operations = redisTemplate.opsForHash();
        return operations.entries(key);
    }

}
