package com.configcenter.service.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class CacheManager {

    private static Logger logger  = LoggerFactory.getLogger(CacheManager.class);

    private static long cacheDuration=20*60*1000L; //缓存分钟

    private static long ZEROLONG=0L;

    public static ConcurrentHashMap<String,Long> lastedCacheTimeMap = new ConcurrentHashMap<String,Long>();

    public static ConcurrentHashMap<String,Object> cacheData= new ConcurrentHashMap<String,Object>();

    public static Object getDataWithCache(String cacheKey,IQueryer queryer) {
        long now = System.currentTimeMillis();
        Long lastedCacheTime = lastedCacheTimeMap.get(cacheKey);
        if(lastedCacheTime==null) {
            lastedCacheTime=ZEROLONG;
        }
        Object data = null;
        if(now-lastedCacheTime<cacheDuration) {
            data= cacheData.get(cacheKey);
            if(data!=null) {
                return data;
            }
        }
        try {
            data = queryer.query();
        } catch (Throwable e) {
            logger.error("CacheManager query error,cacheKey={}",cacheKey,e);
        }
        if(data!=null) {
            cacheData.put(cacheKey,data);
            lastedCacheTimeMap.put(cacheKey,now);
        }
        return data;
    }

    public interface IQueryer {
        public Object query() throws Throwable;
    }
}
