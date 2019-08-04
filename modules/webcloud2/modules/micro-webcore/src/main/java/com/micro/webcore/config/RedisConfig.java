package com.micro.webcore.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@ConditionalOnProperty(prefix = "project.assemble", name = "redis", havingValue = "true",matchIfMissing=true)
public class RedisConfig extends CachingConfigurerSupport {

    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }

    @Bean("redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);
        config(template);
        return template;
    }

    @Bean("redisTemplateForStr")
    public RedisTemplate<String, String> redisTemplateForStr(RedisConnectionFactory factory) {
        RedisTemplate<String, String> template = new RedisTemplate<String, String>();
        template.setConnectionFactory(factory);
        configForStr(template);
        return template;
    }

    private void config(RedisTemplate<String, Object> redisTemplate) {
        //RedisTemplate里面定义了key,value，hashKey,haskValue的序列化器，如果没有设置则会使用默认的JdkSerializationRedisSerializer
        //Jackson2JsonRedisSerializer和GenericJackson2JsonRedisSerializer，两者都能系列化成json，
        //但是GenericJackson2JsonRedisSerializer会在json中加入@class属性，类的全路径包名，方便反系列化。
        //Jackson2JsonRedisSerializer如果存放了List则在反系列化的时候如果没指定TypeReference则会报错java.util.LinkedHashMap cannot be cast to
        //redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
    }

    private void configForStr(RedisTemplate<String, String> redisTemplate) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
    }

}
