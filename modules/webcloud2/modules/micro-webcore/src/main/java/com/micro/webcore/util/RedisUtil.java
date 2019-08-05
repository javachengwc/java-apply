package com.micro.webcore.util;

import org.springframework.data.redis.core.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisUtil {

    //写入缓存
    public static boolean set(RedisTemplate<String,Object> redisTemplate,final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //写入缓存设置时效时间
    public static boolean set(RedisTemplate<String,Object> redisTemplate,final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //加减
    public static Long incrAndDecr(RedisTemplate<String,Object> redisTemplate,final String key, Long incrOrDecrNum) {
        ValueOperations operations = redisTemplate.opsForValue();
        Long count = operations.increment(key, incrOrDecrNum);
        return count;
    }

    //批量删除对应的value
    public static void remove(RedisTemplate<String,Object> redisTemplate,final String... keys) {
        for (String key : keys) {
            remove(redisTemplate,key);
        }
    }

    //批量删除key
    public static void removePattern(RedisTemplate<String,Object> redisTemplate,final String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0)
            redisTemplate.delete(keys);
    }

    //删除对应的value
    public static void remove(RedisTemplate<String,Object> redisTemplate,final String key) {
        if (exists(redisTemplate,key)) {
            redisTemplate.delete(key);
        }
    }

    //删除对应的value，并返回是否删除成功
    public static boolean removeAndReturn(RedisTemplate<String,Object> redisTemplate,final String key) {
        if (exists(redisTemplate,key)) {
            return redisTemplate.getConnectionFactory().getConnection().del(key.getBytes()) == 1;
        }
        return false;
    }

    //判断缓存中是否有对应的value
    public static boolean exists(RedisTemplate<String,Object> redisTemplate,final String key) {
        return redisTemplate.hasKey(key);
    }

    //读取缓存
    public static Object get(RedisTemplate<String,Object> redisTemplate,final String key) {
        Object result = null;
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    //哈希 添加
    public static void hmSet(RedisTemplate<String,Object> redisTemplate,String key, Object hashKey, Object value) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key, hashKey, value);
    }

    //哈希获取数据
    public static Object hmGet(RedisTemplate<String,Object> redisTemplate,String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.get(key, hashKey);
    }

    //哈希数据是否存在
    public static Boolean hmHas(RedisTemplate<String,Object> redisTemplate,String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.hasKey(key, hashKey);
    }

    //列表添加
    public static void lPush(RedisTemplate<String,Object> redisTemplate,String k, Object v) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.rightPush(k, v);
    }

    //列表获取
    public static List<Object> lRange(RedisTemplate<String,Object> redisTemplate,String k, long l, long l1) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(k, l, l1);
    }

    //集合添加
    public static Long add(RedisTemplate<String,Object> redisTemplate,String key, Object value) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        Long result = set.add(key, value);
        return result;
    }

    //集合获取
    public static Set<Object> setMembers(RedisTemplate<String,Object> redisTemplate,String key) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }

    //有序集合添加
    public static void zAdd(RedisTemplate<String,Object> redisTemplate,String key, Object value, double scoure) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key, value, scoure);
    }

    //有序集合获取
    public static Set<Object> rangeByScore(RedisTemplate<String,Object> redisTemplate,String key, double scoure, double scoure1) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key, scoure, scoure1);
    }

    //设置KEY的生命周期
    public static boolean setExpireKey(RedisTemplate<String,Object> redisTemplate,String key, Long seconds, TimeUnit timeUnit) {
        return redisTemplate.expire(key, seconds, timeUnit);
    }

    //获取key剩余过期时间
    public static Long getExpireByKey(RedisTemplate<String,Object> redisTemplate,String key) {
        return redisTemplate.getExpire(key);
    }

    public static Long leftPushAll(RedisTemplate<String,Object> redisTemplate,Object key, Collection values) {
        ListOperations list = redisTemplate.opsForList();
        Long result = list.leftPushAll(key, values);
        return result;
    }

    //list 移除,参考lrem函数,count = 0 : 全部value删除,否则删除count个value
    public static Long lrem(RedisTemplate<String,Object> redisTemplate,String key, long count, Object value) {
        ListOperations list = redisTemplate.opsForList();
        Long result = list.remove(key, count, value);
        return result;
    }

    public static Long rightPushAll(RedisTemplate<String,Object> redisTemplate,String key, Collection values) {
        ListOperations list = redisTemplate.opsForList();
        Long result = list.rightPushAll(key, values);
        return result;
    }

    //srem 删除set中的一个元素
    public static Long srem(RedisTemplate<String,Object> redisTemplate,String key, Object... values) {
        SetOperations setOperations = redisTemplate.opsForSet();
        Long result = setOperations.remove(key, values);
        return result;
    }

    //列表添加 插入到列表头部
    public static void leftPush(RedisTemplate<String,Object> redisTemplate,String k, Object v) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.leftPush(k, v);
    }

    //列表添加 插入到列表头部 所有
    public static void leftPushAll(RedisTemplate<String,Object> redisTemplate,String k, Collection v) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.leftPushAll(k, v);
    }
}
