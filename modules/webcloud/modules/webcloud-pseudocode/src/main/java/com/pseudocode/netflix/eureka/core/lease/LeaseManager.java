package com.pseudocode.netflix.eureka.core.lease;

//服务续约管理器
public interface LeaseManager<T> {

    //服务注册
    void register(T r, int leaseDuration, boolean isReplication);

    //取消( 主动下线 )
    boolean cancel(String appName, String id, boolean isReplication);

    //续约
    boolean renew(String appName, String id, boolean isReplication);

    //过期( 过期下线 )
    void evict();
}

