package com.pseudocode.netflix.eureka.core.registry;

import java.util.concurrent.atomic.AtomicLong;

public interface ResponseCache {

    //过期缓存
    void invalidate(String appName, String vipAddress,String secureVipAddress);

    AtomicLong getVersionDelta();

    AtomicLong getVersionDeltaWithRegions();

    //获取缓存
    String get(Key key);

    //获取缓存并gzip压缩
    byte[] getGZIP(Key key);
}

