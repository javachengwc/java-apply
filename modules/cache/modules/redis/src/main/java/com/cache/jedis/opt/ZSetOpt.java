package com.cache.jedis.opt;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedisPool;

import java.util.Map;
import java.util.Set;

public class ZSetOpt extends BaseOpt {

    public ZSetOpt(JedisPool jedisPool) {
        super(jedisPool);
    }

    public ZSetOpt(ShardedJedisPool shardedJedisPool) {
        super(shardedJedisPool);
    }

    public Long zadd(String key , Map<String, Double> scoreMembers ) {
        Long count = this.execute(key, new RedisInvoker<Long>() {
            @Override
            public Long invoke(Jedis jedis) {
                Long result = jedis.zadd(key, scoreMembers);
                return result;
            }
        });
        return count;
    }

    public Long zcount(String key, double min, double max) {
        Long count = this.execute(key, new RedisInvoker<Long>() {
            @Override
            public Long invoke(Jedis jedis) {
                Long result = jedis.zcount(key, min, max);
                return result;
            }
        });
        return count;
    }

    public Double zincrby(String key,double score,String member ) {
        Double rt =this.execute(key, new RedisInvoker<Double>() {
            @Override
            public Double invoke(Jedis jedis) {
                Double result = jedis.zincrby(key, score, member);
                return result;
            }
        });
        return rt;
    }

    public Set<String> zrangeByScore(String key, double min, double max) {
        Set<String> set = this.execute(key, new RedisInvoker<Set<String>>() {
            @Override
            public Set<String> invoke(Jedis jedis) {
                Set<String> result = jedis.zrangeByScore(key, min, max);
                return result;
            }
        });
        return set;
    }

}
