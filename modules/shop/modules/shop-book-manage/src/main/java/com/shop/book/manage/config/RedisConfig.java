package com.shop.book.manage.config;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig extends CachingConfigurerSupport {

    private RedisProperties properties;

    public RedisConfig(RedisProperties properties) {
        this.properties = properties;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return createJedisConnectionFactory(this.properties);
    }

    @Bean("redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);
        config(template);
        return template;
    }

    //专为计数用的缓存
    @Bean("redisTemplateForStr")
    public RedisTemplate<String, String> redisTemplateForStr(JedisConnectionFactory factory) {
        RedisTemplate<String, String> template = new RedisTemplate<String, String>();
        template.setConnectionFactory(factory);
        configForStr(template);
        return template;
    }

    private JedisConnectionFactory createJedisConnectionFactory(RedisProperties properties) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        RedisProperties.Pool poolProps = properties.getPool();
        if (poolProps != null) {
            poolConfig.setMaxTotal(poolProps.getMaxActive());
            poolConfig.setMaxIdle(poolProps.getMaxIdle());
            poolConfig.setMinIdle(poolProps.getMinIdle());
            poolConfig.setMaxWaitMillis(poolProps.getMaxWait());
        }
        JedisConnectionFactory factory = new JedisConnectionFactory(poolConfig);
        factory.setHostName(properties.getHost());
        factory.setPort(properties.getPort());
        if (properties.getPassword() != null) {
            factory.setPassword(properties.getPassword());
        }
        factory.setDatabase(properties.getDatabase());
        if (properties.getTimeout() > 0) {
            factory.setTimeout(properties.getTimeout());
        }
        return factory;
    }

    private void config(RedisTemplate<String, Object> redisTemplate) {
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        redisTemplate.setKeySerializer(new StringRedisSerializer());
    }

    private void configForStr(RedisTemplate<String, String> redisTemplate) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
    }

    @Override
    public CacheManager cacheManager() {
        return super.cacheManager();
    }
}
