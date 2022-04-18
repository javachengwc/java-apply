package com.cache.jedis.opt;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedisPool;

import java.util.Map;

public class HashOpt extends BaseOpt {

    public HashOpt(JedisPool jedisPool) {
        super(jedisPool);
    }

    public HashOpt(ShardedJedisPool shardedJedisPool) {
        super(shardedJedisPool);
    }

    //获取key缓存的hash数据，如果数据量过大，谨慎使用
    public Map<String, String> hgetAll(String key) {
        Map<String, String> result = this.execute(key, new RedisInvoker<Map<String, String>>() {
            @Override
            public Map<String, String> invoke(Jedis jedis) {
                Map<String, String> result = jedis.hgetAll(key);
                return result;
            }
        });
        return result;
    }

    public Long hincrBy(String key, String field, long value) {
        Long result =this.execute(key, new RedisInvoker<Long>() {
            @Override
            public Long invoke(Jedis jedis) {
                Long result = jedis.hincrBy(key, field, value);
                return result;
            }
        });
        return  result;
    }
}
