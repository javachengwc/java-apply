package com.cache.redis.demo;

import com.cache.model.DemoEntity;
import com.util.date.CalendarUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DemoEntityService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**redis的hash操作**/
    public void doHash()
    {

        redisTemplate.setHashValueSerializer(new JacksonJsonRedisSerializer<DemoEntity>(DemoEntity.class));
        HashOperations operations = redisTemplate.opsForHash();

        String key="hash-key";


        operations.put(key, 1, new DemoEntity(1,"a") );
        operations.put(key, 2, new DemoEntity(2,"b") );

        redisTemplate.expireAt(key, CalendarUtil.addDates(new Date(),1));

        List<DemoEntity> list = operations.values(key);

        operations.delete(key, 1);

    }

    /**redis的string操作**/
    public void opString()
    {
        String key = "str_key";
        String value = (String) redisTemplate.opsForValue().get(key);

        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, 3, TimeUnit.MINUTES);

    }

    
}
