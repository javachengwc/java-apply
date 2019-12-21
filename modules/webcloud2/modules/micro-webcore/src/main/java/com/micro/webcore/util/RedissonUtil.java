package com.micro.webcore.util;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedissonUtil {

  @Autowired(required = false)
  RedissonClient redissonClient;

  private static RedissonUtil redissonUtil;

  public static  RedissonUtil getRedissonUtilBean() {
    if(redissonUtil==null) {
      redissonUtil= SpringContextUtil.getBean(RedissonUtil.class);
    }
    return redissonUtil;
  }

  public static boolean lock(String name, long leaseTime) {
    boolean rt = RedissonUtil.getRedissonUtilBean()._lock(name,leaseTime);
    return rt;
  }

  public static void unlock(String name) {
    RedissonUtil.getRedissonUtilBean()._unlock(name);
  }

  public boolean _lock(String name, long expire) {
    try {
      boolean rt =redissonClient.getLock(name).tryLock(expire, TimeUnit.SECONDS);
      return rt;
    } catch (Exception e) {
      log.error("RedissonUtil lock [" + name + "] error", e);
      return false;
    }
  }

  public void _unlock(String name) {
    try {
      redissonClient.getLock(name).unlock();
    } catch (Exception e) {
      log.error("RedissonUtil unlock [" + name + "] error", e);
    }
  }

}


