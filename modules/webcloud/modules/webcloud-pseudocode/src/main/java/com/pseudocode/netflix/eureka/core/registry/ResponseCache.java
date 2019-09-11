package com.pseudocode.netflix.eureka.core.registry;

import java.util.concurrent.atomic.AtomicLong;

public interface ResponseCache {

    void invalidate(String appName, String vipAddress,String secureVipAddress);

    AtomicLong getVersionDelta();

    AtomicLong getVersionDeltaWithRegions();

    String get(Key key);

    byte[] getGZIP(Key key);
}

