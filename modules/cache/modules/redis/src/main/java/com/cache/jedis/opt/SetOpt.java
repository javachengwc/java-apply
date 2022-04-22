package com.cache.jedis.opt;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedisPool;

import java.util.Set;

public class SetOpt extends BaseOpt {

    public SetOpt(JedisPool jedisPool) {
        super(jedisPool);
    }

    public SetOpt(ShardedJedisPool shardedJedisPool) {
        super(shardedJedisPool);
    }

    public Set<String> smembers(String key) {
        Set<String> rt = this.execute(key, new RedisInvoker<Set<String>>() {
            @Override
            public Set<String> invoke(Jedis jedis) {
                Set<String> result = jedis.smembers(key);
                return result;
            }
        });
        return rt;
    }

    public Boolean sismember(String key, String member) {
        Boolean rt = this.execute(key, new RedisInvoker<Boolean>() {
            @Override
            public Boolean invoke(Jedis jedis) {
                Boolean result = jedis.sismember(key, member);
                return result;
            }
        });
        return rt;
    }

    public Long sadd(String key , String... values ) {
        Long rt = this.execute(key, new RedisInvoker<Long>() {
            @Override
            public Long invoke(Jedis jedis) {
                Long result = jedis.sadd(key, values);
                return result;
            }
        });
        return rt;
    }

    public Long srem(String key , String... values ) {
        Long rt = this.execute(key, new RedisInvoker<Long>() {
            @Override
            public Long invoke(Jedis jedis) {
                Long result = jedis.srem(key, values);
                return result;
            }
        });
        return rt;
    }
}
