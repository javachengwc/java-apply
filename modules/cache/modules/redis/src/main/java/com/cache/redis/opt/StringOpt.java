package com.cache.redis.opt;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.TimeUnit;

public class StringOpt extends BaseOpt{

    public StringOpt(RedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    public void redisSerial() {
        //只有GenericToStringSerializer、StringRedisSerializer将字符串的值直接转为字节数组，保存到redis中数字字符串是数字，可以进行加减
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
    }

    public void set(String key, String value) {
        if(redisLogger.isDebugEnabled()) {
            redisLogger.debug("StringOpt set start, key={},value={}",key,value);
        }
        redisSerial();
        ValueOperations<String, String> opt =redisTemplate.opsForValue();
        opt.set(key,value);
    }

    public void set(String key, String value,Long timeout,TimeUnit timeUnit) {
        if(redisLogger.isDebugEnabled()) {
            redisLogger.debug("StringOpt set  with timeout start, key={},value={}",key,value);
        }
        redisSerial();
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    //不存在才加入
    public boolean setIfAbsent(String key, String value) {
        if(redisLogger.isDebugEnabled()) {
            redisLogger.debug("StringOpt setIfAbsent start, key={},value={}",key,value);
        }
        redisSerial();
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    //长度
    public Long length(String key) {
        redisSerial();
        return redisTemplate.opsForValue().size(key);
    }

    //可不初始化，直接加减值
    //累加,返回增加后的值
    public long add(String key, Long value) {
        if(redisLogger.isDebugEnabled()) {
            redisLogger.debug("StringOpt add start, key={},value={}",key,value);
        }
        redisSerial();
        return redisTemplate.opsForValue().increment(key, value);
    }

    //可不初始化，直接加减值
    public double addDouble(String key,Double value) {
        if(redisLogger.isDebugEnabled()) {
            redisLogger.debug("StringOpt addDouble start, key={},value={}",key,value);
        }
        redisSerial();
        return redisTemplate.opsForValue().increment(key, value);
    }

    public String get(String key) {
        redisSerial();
        Object obj = redisTemplate.opsForValue().get(key);
        return obj==null?null:obj.toString();
    }
}
