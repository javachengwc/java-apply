package com.cache.springredis.service;

import com.cache.springredis.model.vo.DataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListService {

    private static String LIST_CACHE_KEY="LIST";

    @Autowired
    private RedisTemplate redisTemplate;

    //添加数据
    public boolean addData(DataVo data) {
        String key = LIST_CACHE_KEY;
        redisTemplate.opsForList().leftPush(key, data);
        return true;
    }

    //查询数据列表
    public List<DataVo> listData(Integer count ) {
        String key = LIST_CACHE_KEY;
        List<DataVo> list = redisTemplate.opsForList().range(key, 0, count-1);
        return list;
    }

    //查询data数量
    public Long countData() {
        String key = LIST_CACHE_KEY;
        Long count = redisTemplate.opsForList().size(key);
        return count;
    }

}
