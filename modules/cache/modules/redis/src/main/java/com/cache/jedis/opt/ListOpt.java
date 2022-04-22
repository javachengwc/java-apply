package com.cache.jedis.opt;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedisPool;

public class ListOpt extends BaseOpt {

    public ListOpt(JedisPool jedisPool) {
        super(jedisPool);
    }

    public ListOpt(ShardedJedisPool shardedJedisPool) {
        super(shardedJedisPool);
    }

    public Long llen(String key) {
        Long len = this.execute(key, new RedisInvoker<Long>() {
            @Override
            public Long invoke(Jedis jedis) {
                Long result = jedis.llen(key);
                return result;
            }
        });
        return len;
    }

    public Long lpush(String key , String... values ) {
        Long rt = this.execute(key, new RedisInvoker<Long>() {
            @Override
            public Long invoke(Jedis jedis) {
                Long result = jedis.lpush(key, values);
                return result;
            }
        });
        return rt;
    }
}
