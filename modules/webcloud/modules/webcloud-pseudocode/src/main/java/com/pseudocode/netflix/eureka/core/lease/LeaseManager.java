package com.pseudocode.netflix.eureka.core.lease;

//服务续约管理器
public interface LeaseManager<T> {

    //注册
    void register(T r, int leaseDuration, boolean isReplication);

    //删除
    boolean cancel(String appName, String id, boolean isReplication);

    //续约
    boolean renew(String appName, String id, boolean isReplication);

    //驱逐
    void evict();
}

