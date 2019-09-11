package com.pseudocode.netflix.eureka.client.discovery.shared.transport;

public interface EurekaTransportConfig {

    //EurekaHttpClient 会话周期性重连时间，单位：秒
    int getSessionedClientReconnectIntervalSeconds();

    //重试 EurekaHttpClient ，请求失败的 Eureka-Server 隔离集合占比 Eureka-Server 全量集合占比，超过该比例，进行清空
    double getRetryableClientQuarantineRefreshPercentage();

    int getApplicationsResolverDataStalenessThresholdSeconds();

    boolean applicationsResolverUseIp();

    //异步解析 EndPoint 集群频率，单位：毫秒
    int getAsyncResolverRefreshIntervalMs();

    //异步解析器预热解析 EndPoint 集群超时时间，单位：毫秒
    int getAsyncResolverWarmUpTimeoutMs();

    //异步解析器线程池大小
    int getAsyncExecutorThreadPoolSize();

    String getWriteClusterVip();

    String getReadClusterVip();

    String getBootstrapResolverStrategy();

    boolean useBootstrapResolverForQuery();
}

