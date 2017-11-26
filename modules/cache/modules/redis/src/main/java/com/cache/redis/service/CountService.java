package com.cache.redis.service;

import com.cache.redis.opt.StringOpt;
import com.util.base.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

//降数服务
@Service
public class CountService {

    @Autowired
    private RedisTemplate redisTemplate;

    private StringOpt stringOpt;

    @PostConstruct
    public void initOpt() {
        stringOpt = new StringOpt(redisTemplate);
    }

    //可不初始化，直接加减值
    public long decrCount(String key,long count) {
       return stringOpt.add( key,-count);
    }

    //可不初始化，直接加减值
    public long incrCount(String key,long count) {
        return stringOpt.add( key,count);
    }

    public void iniCount(String key,long count) {
        stringOpt.set(key, "" + count);
    }

    public boolean iniCountIfAbsent(String key,long count) {
        boolean rt =stringOpt.setIfAbsent(key,""+count);
        return rt;
    }

    public Long getCount(String key) {

        String str= stringOpt.get(key);
        if(NumberUtil.isNumeric(str)) {
            return Long.parseLong(str);
        }
        return null;
    }
}
