package com.cache.redis.opt;

import org.springframework.data.redis.core.RedisTemplate;

public class ZSetOpt extends BaseOpt{

    public ZSetOpt(RedisTemplate redisTemplate) {
        super(redisTemplate);
    }


}
