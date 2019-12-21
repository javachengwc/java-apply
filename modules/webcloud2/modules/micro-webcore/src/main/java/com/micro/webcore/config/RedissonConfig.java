package com.micro.webcore.config;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(org.redisson.config.Config.class)
@EnableConfigurationProperties(RedisProperties.class)
@ConditionalOnProperty(prefix = "project.assemble", name = "redisson", havingValue = "true",matchIfMissing=false)
public class RedissonConfig {

  @Bean
  public RedissonClient redissonClient(RedisProperties prop){
    org.redisson.config.Config config = new Config();
    SingleServerConfig serverConfig = config.useSingleServer()
        .setAddress("redis://"+prop.getHost()+":"+prop.getPort())
        .setTimeout((int)prop.getTimeout().getSeconds())
        .setDatabase(prop.getDatabase())
        .setConnectionPoolSize(prop.getLettuce().getPool().getMaxActive())
        .setConnectionMinimumIdleSize(prop.getLettuce().getPool().getMinIdle());
    if(StringUtils.isNotEmpty(prop.getPassword())){
      serverConfig.setPassword(prop.getPassword());
    }
    return Redisson.create(config);
  }
}

