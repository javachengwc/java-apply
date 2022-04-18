package com.cache.jedis.opt;

import redis.clients.jedis.Jedis;

public interface RedisInvoker<T> {

    T invoke(Jedis jedis);
}