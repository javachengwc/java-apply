package com.captcha.util;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.rubyeye.xmemcached.MemcachedClient;

@Component
public final class MemcachedUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemcachedUtil.class);

    private static MemcachedClient staticMemcachedClient;

    @Autowired
    private MemcachedClient memcachedClient;

    /**
     * 用一个静态MemcachedClient引用指向Spring容器中的单例memcachedClient
     */
    @PostConstruct
    @SuppressWarnings("unused")
    private void memcachedClientInit() {
        MemcachedUtil.staticMemcachedClient = memcachedClient;
    }

    public static boolean put(String key, Object value, int expiry) {
        try {
            System.out.println("put "+key+"="+value);
            return staticMemcachedClient.set(key, expiry, value);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    public static boolean remove(String key) {
        try {
            return staticMemcachedClient.delete(key);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        try {
            return (T)staticMemcachedClient.get(key);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    public static boolean replace(String key, Object value, int expiry) {
        try {
            return staticMemcachedClient.replace(key, expiry, value);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

}
