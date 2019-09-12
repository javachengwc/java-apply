package com.pseudocode.netflix.eureka.core.lease;

public class Lease<T> {

    enum Action {
        Register, Cancel, Renew
    };

    public static final int DEFAULT_DURATION_IN_SECS = 90;

    //实体
    private T holder;
    //取消注册时间戳
    private long evictionTimestamp;
    //注册时间戳
    private long registrationTimestamp;
    //开始服务时间戳
    private long serviceUpTimestamp;
    // Make it volatile so that the expiration task would see this quicker
    //最后更新时间戳,每次续租时，更新该时间戳
    private volatile long lastUpdateTimestamp;
    //租约持续时长，单位：毫秒
    private long duration;

    public Lease(T r, int durationInSecs) {
        holder = r;
        registrationTimestamp = System.currentTimeMillis();
        lastUpdateTimestamp = registrationTimestamp;
        duration = (durationInSecs * 1000);

    }

    public void renew() {
        lastUpdateTimestamp = System.currentTimeMillis() + duration;

    }

    public void cancel() {
        if (evictionTimestamp <= 0) {
            evictionTimestamp = System.currentTimeMillis();
        }
    }

    public void serviceUp() {
        if (serviceUpTimestamp == 0) {
            serviceUpTimestamp = System.currentTimeMillis();
        }
    }

    public void setServiceUpTimestamp(long serviceUpTimestamp) {
        this.serviceUpTimestamp = serviceUpTimestamp;
    }

    public boolean isExpired() {
        return isExpired(0l);
    }

    //判断租约是否过期
    public boolean isExpired(long additionalLeaseMs) {
        return (evictionTimestamp > 0 || System.currentTimeMillis() > (lastUpdateTimestamp + duration + additionalLeaseMs));
    }

    public long getRegistrationTimestamp() {
        return registrationTimestamp;
    }

    public long getLastRenewalTimestamp() {
        return lastUpdateTimestamp;
    }

    public long getEvictionTimestamp() {
        return evictionTimestamp;
    }

    public long getServiceUpTimestamp() {
        return serviceUpTimestamp;
    }

    public T getHolder() {
        return holder;
    }

}

