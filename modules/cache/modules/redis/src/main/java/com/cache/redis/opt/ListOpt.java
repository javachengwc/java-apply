package com.cache.redis.opt;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

public class ListOpt extends BaseOpt{

    public ListOpt(RedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    public List<String> queryList(String key, long start, long end) {
        if(redisLogger.isDebugEnabled()) {
            redisLogger.debug("ListOpt queryList start, key={},start={},end={}",key,start,end);
        }
        return redisTemplate.opsForList().range(key, start, end);
    }

    public Long size(String key) {
        return redisTemplate.opsForList().size(key);
    }

    public Long leftPush(String key, String value) {
        if(redisLogger.isDebugEnabled()) {
            redisLogger.debug("ListOpt leftPush start, key={},value={}",key,value);
        }
        return redisTemplate.opsForList().leftPush(key, value);
    }

    public Long rightPush(String key, String value) {
        if(redisLogger.isDebugEnabled()) {
            redisLogger.debug("ListOpt rightPush start, key={},value={}",key,value);
        }
        return redisTemplate.opsForList().rightPush(key, value);
    }

    public String leftPop(String key) {
        if(redisLogger.isDebugEnabled()) {
            redisLogger.debug("ListOpt leftPop start, key={}",key);
        }
        return (String)redisTemplate.opsForList().leftPop(key);
    }

    public String rightPop(String key) {
        if(redisLogger.isDebugEnabled()) {
            redisLogger.debug("ListOpt rightPop start, key={}",key);
        }
        return (String)redisTemplate.opsForList().rightPop(key);
    }

    public void set(String key, long index, String value) {
        if(redisLogger.isDebugEnabled()) {
            redisLogger.debug("ListOpt set start, key={},index={},value={}",key,index,value);
        }
        redisTemplate.opsForList().set(key, index, value);
    }

    public String index(String key, long index) {
        return (String)redisTemplate.opsForList().index(key, index);
    }

    public ListOperations<String, String> getListOperations() {
        return redisTemplate.opsForList();
    }

}
