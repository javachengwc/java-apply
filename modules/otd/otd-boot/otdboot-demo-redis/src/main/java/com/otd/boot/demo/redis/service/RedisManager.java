package com.otd.boot.demo.redis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.apache.shiro.session.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisManager implements InitializingBean,ApplicationContextAware {

    public static Logger logger= LoggerFactory.getLogger(RedisManager.class);

    private RedisTemplate<String, Session> redisTemplate;

    private RedisTemplate<String, String> stringRedisTemplate;

    private  String KEY = "KEY:";

    private Integer expire =30 * 60 ;

    @Autowired(required=false)
    private RedisConnectionFactory redisConnectionFactory;

    public RedisTemplate<String, Session> getRedisTemplate() {
        return redisTemplate;
    }

    public RedisTemplate<String, String> getStringRedisTemplate() {
        return stringRedisTemplate;
    }

    private String getKey(String key) {
        return KEY+":"+key;
    }

    public void add(String sessionId, Session session){
        redisTemplate.opsForValue().set(getKey(sessionId), session);
        redisTemplate.expire(getKey(sessionId),expire, TimeUnit.SECONDS);
    }

    public void delete(String sessionId){
        redisTemplate.delete(getKey(sessionId));
    }

    public Session get(String sessionId){
        redisTemplate.expire(getKey(sessionId),expire,TimeUnit.SECONDS);
        Session session=	redisTemplate.opsForValue().get(getKey(sessionId));
        return  session ;
    }

    public List<Session> mget(){
        Set<String> keys =redisTemplate.keys(KEY+":"+"*");
        if(keys == null || keys.size() > 0){
            return new ArrayList<Session>();
        }
        List<Session> list =redisTemplate.opsForValue().multiGet(keys);
        return list;
    }

    public String getKEY() {
        return KEY;
    }

    public void setKEY(String kEY) {
        KEY = kEY;
    }

    public RedisConnectionFactory getRedisConnectionFactory() {
        return redisConnectionFactory;
    }

    public void setRedisConnectionFactory(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        redisTemplate = new RedisTemplate<String, Session>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.afterPropertiesSet();

        stringRedisTemplate = new RedisTemplate<String, String>();
        stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
        stringRedisTemplate.setHashKeySerializer(new StringRedisSerializer());
        stringRedisTemplate.setHashValueSerializer(new StringRedisSerializer());
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        stringRedisTemplate.afterPropertiesSet();
        logger.info("RedisManager afterPropertiesSet end....");
    }

}
