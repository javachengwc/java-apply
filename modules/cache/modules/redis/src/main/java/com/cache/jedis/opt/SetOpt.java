package com.cache.jedis.opt;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedisPool;

public class SetOpt extends BaseOpt {

    public SetOpt(JedisPool jedisPool) {
        super(jedisPool);
    }

    public SetOpt(ShardedJedisPool shardedJedisPool) {
        super(shardedJedisPool);
    }
}
