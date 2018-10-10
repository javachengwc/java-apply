package com.shop.book.service.manager;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheManager {

    private static Logger logger  = LoggerFactory.getLogger(CacheManager.class);

    private static long cacheDuration=20*60*1000L; //缓存20分钟

    private static long ZEROLONG=0L;

    public static ConcurrentHashMap<String,Long> lastedCacheTimeMap = new ConcurrentHashMap<String,Long>();

    public static ConcurrentHashMap<String,Object> cacheData= new ConcurrentHashMap<String,Object>();

    public static Object  getDataWithCache(String cacheKey,IQueryer queryer) {
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
            data = queryer.queryData();
        } catch (Throwable e) {
            logger.error("CacheManager queryData error,cacheKey={}",cacheKey,e);
        }
        if(data!=null) {
            cacheData.put(cacheKey,data);
            lastedCacheTimeMap.put(cacheKey,now);
        }
        return data;
    }

    public static List<String> getAllCacheKey() {

        return new ArrayList<String>(lastedCacheTimeMap.keySet());
    }

    public static Map<String,Long> getAllCache() {

        return new HashMap<String,Long>(lastedCacheTimeMap);
    }

    public static Boolean cleanCache(String cacheKey) {
        for(Map.Entry<String,Long> entry:lastedCacheTimeMap.entrySet()) {
            String key = entry.getKey();
            if(StringUtils.isBlank(cacheKey) || key.startsWith(cacheKey)) {
                lastedCacheTimeMap.put(key,ZEROLONG);
            }
        }
        return true;
    }

    public interface IQueryer {
        public Object queryData() throws Throwable;
    }
}

