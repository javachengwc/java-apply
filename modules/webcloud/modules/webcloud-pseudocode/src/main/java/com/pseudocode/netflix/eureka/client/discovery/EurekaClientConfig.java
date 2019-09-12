package com.pseudocode.netflix.eureka.client.discovery;

import com.pseudocode.netflix.eureka.client.discovery.shared.transport.EurekaTransportConfig;

import java.util.List;

//import com.google.inject.ImplementedBy;

//@ImplementedBy(DefaultEurekaClientConfig.class)
public interface EurekaClientConfig {

    //从Eureka服务端获取注册信息的间隔时间，默认30秒
    int getRegistryFetchIntervalSeconds();

    //更新实例信息的变化到Eureka服务端的间隔时间，默认30秒
    int getInstanceInfoReplicationIntervalSeconds();

    //初始化实例信息到Eureka服务端的间隔时间，默认40秒
    int getInitialInstanceInfoReplicationIntervalSeconds();

    int getEurekaServiceUrlPollIntervalSeconds();

    String getProxyHost();

    String getProxyPort();

    String getProxyUserName();

    String getProxyPassword();

    boolean shouldGZipContent();

    int getEurekaServerReadTimeoutSeconds();

    int getEurekaServerConnectTimeoutSeconds();

    String getBackupRegistryImpl();

    int getEurekaServerTotalConnections();

    int getEurekaServerTotalConnectionsPerHost();

    String getEurekaServerURLContext();

    //Eureka-Server 的端口
    String getEurekaServerPort();

    //Eureka-Server 的 DNS 名
    String getEurekaServerDNSName();

    //是否使用 DNS 方式获取 Eureka-Server URL 地址
    boolean shouldUseDnsForFetchingServiceUrls();

    boolean shouldRegisterWithEureka();

    default boolean shouldUnregisterOnShutdown() {
        return true;
    }

    boolean shouldPreferSameZoneEureka();

    boolean allowRedirects();

    //是否打印增量和全量差异。默认值:false.通过eureka.client.printDeltaFullDiff设置
    //开启该参数会导致每次增量获取后又发起全量获取，不要开启
    boolean shouldLogDeltaDiff();

    //是否禁用增量获取注册信息,默认值：false,通过eureka.client.disableDelta设置
    boolean shouldDisableDelta();

    String fetchRegistryForRemoteRegions();

    //Eureka-Client所在区域,通过eureka.client.region属性定义
    String getRegion();

    //Eureka-Client所在地区( region )可用区( zone )集合，通过eureka.client.availability-zones属性进行设置,
    //当没有为Region配置Zone的时，将默认采用defaultZone,通过eureka.client.serviceUrl.defaultZone属性定义
    //Region与Zone是一对多的关系
    String[] getAvailabilityZones(String region);

    List<String> getEurekaServerServiceUrls(String myZone);

    //是否只保留状态为开启( UP )的应用实例，默认值:true,通过eureka.client.shouldFilterOnlyUpInstances设置
    boolean shouldFilterOnlyUpInstances();

    int getEurekaConnectionIdleTimeoutSeconds();

    //通过eureka.client.fetch-registry=true属性定义
    boolean shouldFetchRegistry();

    String getRegistryRefreshSingleVipAddress();

    int getHeartbeatExecutorThreadPoolSize();

    //心跳超时重试延迟时间的最大乘数值,默认值10
    int getHeartbeatExecutorExponentialBackOffBound();

    int getCacheRefreshExecutorThreadPoolSize();

    //缓存刷新重试延迟时间的最大乘数值
    int getCacheRefreshExecutorExponentialBackOffBound();

    String getDollarReplacement();

    String getEscapeCharReplacement();

    //当eureka.client.shouldOnDemandUpdateStatusChange = true，InstanceInfo 的状态( status ) 属性发生变化时，会立即向 Eureka-Server 注册
    boolean shouldOnDemandUpdateStatusChange();

    default boolean shouldEnforceRegistrationAtInit() {
        return false;
    }

    String getEncoderName();

    String getDecoderName();

    String getClientDataAccept();

    String getExperimental(String name);

    EurekaTransportConfig getTransportConfig();
}
