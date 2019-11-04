package com.cache.redis.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.TimeUnit;

public class RedisLockUtil {

    private static Logger logger= LoggerFactory.getLogger(RedisLockUtil.class);

    private static Integer defaultExpire = 10*1000; //10秒

    private static volatile RedisTemplate<String,Object> redisTemplate = null;

    public static RedisTemplate gainRedisTemplate() {
        if(redisTemplate==null) {
            redisTemplate = (RedisTemplate<String,Object> )SpringContextUtil.getBean(RedisTemplate.class);
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(new StringRedisSerializer());
        }
        return redisTemplate;
    }

    //加锁
    //基于 redis 的 setnx()、expire() 方法做分布式锁
    //此方案从技术上说,有欠缺，比如，setnx()执行后，expire() 执行前，发生宕机，依然会出现死锁的问题，
    //所以如果要对其进行完善，可以使用 redis 的 setnx()、get() 和 getset() 方法来实现分布式锁。
    public static boolean lockA(String key, int expire) {
        RedisTemplate redisTemplate =gainRedisTemplate();
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        boolean rt =valueOperations.setIfAbsent(key,"1");
        if(rt) {
            redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS);
            return true;
        }
        return false;
    }

    //解锁
    public static void unLockA(String key) {
        RedisTemplate redisTemplate = gainRedisTemplate();
        redisTemplate.delete(key);
    }

    //加锁
    //基于 redis 的 setnx()、get() 和 getset() 方法来实现分布式锁
    public static boolean lock(String key) {

        RedisTemplate redisTemplate =gainRedisTemplate();
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        long value = System.currentTimeMillis() + defaultExpire; //过期时间戳
        boolean rt = valueOperations.setIfAbsent(key, String.valueOf(value));
        if(rt) {
            return true;
        }
        String oldValue =valueOperations.get(key);
        long oldExpireTime=0L;
        if(!StringUtils.isBlank(oldValue)) {
            oldExpireTime=Long.parseLong(oldValue);
        }
        if(oldExpireTime < System.currentTimeMillis()) {
            //超时
            long newExpireTime = System.currentTimeMillis() + defaultExpire;
            //原来值
            String originValue = valueOperations.getAndSet(key, String.valueOf(newExpireTime));

            //两个原来值是否相等
            if(!StringUtils.isBlank(originValue) &&  originValue.equals(oldValue)) {
                return true;
            }
        }
        return false;
    }

    public static void unLock(String key) {
        RedisTemplate redisTemplate =gainRedisTemplate();
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        String oldValue =valueOperations.get(key);
        long oldExpireTime=0L;
        if(!StringUtils.isBlank(oldValue)) {
            oldExpireTime=Long.parseLong(oldValue);
        }
        if(oldExpireTime > System.currentTimeMillis()) {
            redisTemplate.delete(key);
        }
    }

}
