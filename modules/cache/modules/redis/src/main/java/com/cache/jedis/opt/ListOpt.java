package com.cache.jedis.opt;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedisPool;

public class ListOpt extends BaseOpt {

    public ListOpt(JedisPool jedisPool) {
        super(jedisPool);
    }

    public ListOpt(ShardedJedisPool shardedJedisPool) {
        super(shardedJedisPool);
    }

}
