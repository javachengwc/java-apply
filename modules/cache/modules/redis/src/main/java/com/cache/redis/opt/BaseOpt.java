package com.cache.redis.opt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BaseOpt {

    public Logger redisLogger= LoggerFactory.getLogger(getClass());

    protected RedisTemplate redisTemplate;

    public BaseOpt(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Boolean expire(String key,Long timeout,TimeUnit timeUnit) {
        return redisTemplate.expire(key,timeout,timeUnit);
    }

    public Boolean expireAt(String key,Date date) {
        return redisTemplate.expireAt(key,date);
    }


    public void delete(String key) {
        if(redisLogger.isDebugEnabled()) {
            redisLogger.debug("BaseOpt delete start, key={}",key);
        }
        redisTemplate.delete(key);
    }

}
