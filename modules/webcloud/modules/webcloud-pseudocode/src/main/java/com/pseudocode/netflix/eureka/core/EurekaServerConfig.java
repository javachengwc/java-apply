package com.pseudocode.netflix.eureka.core;

import java.util.Map;
import java.util.Set;

public interface EurekaServerConfig {

    String getAWSAccessId();

    String getAWSSecretKey();

    int getEIPBindRebindRetries();

    int getEIPBindingRetryIntervalMsWhenUnbound();

    int getEIPBindingRetryIntervalMs();

    //是否开启自我保护机制
    boolean shouldEnableSelfPreservation();

    //开启自我保护模式比例，超过该比例后开启自我保护模式
    double getRenewalPercentThreshold();

    //自我保护模式比例更新定时任务执行频率，单位：毫秒
    int getRenewalThresholdUpdateIntervalMs();

    //Eureka-Server 集群节点更新频率，单位：毫秒
    int getPeerEurekaNodesUpdateIntervalMs();

    boolean shouldEnableReplicatedRequestCompression();

    int getNumberOfReplicationRetries();

    int getPeerEurekaStatusRefreshTimeIntervalMs();

    //Eureka-Server 启动时，从远程 Eureka-Server 读取不到注册信息时，多长时间不允许 Eureka-Client 访问
    int getWaitTimeInMsWhenSyncEmpty();

    int getPeerNodeConnectTimeoutMs();

    int getPeerNodeReadTimeoutMs();

    int getPeerNodeTotalConnections();

    int getPeerNodeTotalConnectionsPerHost();

    int getPeerNodeConnectionIdleTimeoutSeconds();

    //租约变更记录过期时长，单位：毫秒。默认值: 3601000 毫秒
    long getRetentionTimeInMSInDeltaQueue();

    //移除队列里过期的租约变更记录的定时任务执行频率，单位：毫秒。默认值:30*1000 毫秒
    long getDeltaRetentionTimerIntervalInMs();

    //租约过期定时任务执行频率，单位：毫秒
    long getEvictionIntervalTimerInMs();

    int getASGQueryTimeoutMs();

    long getASGUpdateIntervalMs();

    long getASGCacheExpiryTimeoutMs();

    //读写缓存写入后过期时间，单位：秒
    long getResponseCacheAutoExpirationInSeconds();

    //只读缓存更新频率，单位：毫秒
    long getResponseCacheUpdateIntervalMs();

    //是否开启只读请求响应缓存
    //响应缓存 ( ResponseCache ) 机制目前使用两层缓存策略。优先读取只读缓存，读取不到后读取固定过期的读写缓存
    boolean shouldUseReadOnlyResponseCache();

    boolean shouldDisableDelta();

    long getMaxIdleThreadInMinutesAgeForStatusReplication();

    int getMinThreadsForStatusReplication();

    int getMaxThreadsForStatusReplication();

    int getMaxElementsInStatusReplicationPool();

    //是否同步应用实例信息，当应用实例信息最后更新时间戳( lastDirtyTimestamp )发生改变
    boolean shouldSyncWhenTimestampDiffers();

    //Eureka-Server 启动时，从远程 Eureka-Server 读取失败重试次数
    int getRegistrySyncRetries();

    //Eureka-Server 启动时，从远程 Eureka-Server 读取失败等待( sleep )间隔，单位：毫秒
    long getRegistrySyncRetryWaitMs();

    //待执行同步应用实例信息事件缓冲最大数量
    int getMaxElementsInPeerReplicationPool();

    long getMaxIdleThreadAgeInMinutesForPeerReplication();

    int getMinThreadsForPeerReplication();

    //同步应用实例信息最大线程数
    int getMaxThreadsForPeerReplication();

    int getHealthStatusMinNumberOfAvailablePeers();

    //执行单个同步应用实例信息状态任务最大时间
    int getMaxTimeForReplication();

    boolean shouldPrimeAwsReplicaConnections();

    boolean shouldDisableDeltaForRemoteRegions();

    int getRemoteRegionConnectTimeoutMs();

    int getRemoteRegionReadTimeoutMs();

    int getRemoteRegionTotalConnections();

    int getRemoteRegionTotalConnectionsPerHost();

    int getRemoteRegionConnectionIdleTimeoutSeconds();

    boolean shouldGZipContentFromRemoteRegion();

    Map<String, String> getRemoteRegionUrlsWithName();

    String[] getRemoteRegionUrls();

    Set<String> getRemoteRegionAppWhitelist(String regionName);

    int getRemoteRegionRegistryFetchInterval();

    int getRemoteRegionFetchThreadPoolSize();

    String getRemoteRegionTrustStore();

    String getRemoteRegionTrustStorePassword();

    //是否禁用本地读取不到注册信息，从远程 Eureka-Server 读取
    boolean disableTransparentFallbackToOtherRegion();

    boolean shouldBatchReplication();

    //是否打印访问的客户端名和版本号
    boolean shouldLogIdentityHeaders();

    //请求限流是否开启
    boolean isRateLimiterEnabled();

    //是否对标准客户端判断是否限流。
    //标准客户端通过请求头( header )的 "DiscoveryIdentity-Name" 来判断，是否在标准客户端名集合里。
    boolean isRateLimiterThrottleStandardClients();

    //标准客户端名集合。默认包含"DefaultClient" 和 "DefaultServer"
    Set<String> getRateLimiterPrivilegedClients();

    //速率限制的 burst size ，使用令牌桶算法
    int getRateLimiterBurstSize();

    //增量拉取注册信息的速率限制
    int getRateLimiterRegistryFetchAverageRate();

    //全量拉取注册信息的速率限制
    int getRateLimiterFullFetchAverageRate();

    String getListAutoScalingGroupsRoleName();

    String getJsonCodecName();

    String getXmlCodecName();

//    AwsBindingStrategy getBindingStrategy();

    long getRoute53DomainTTL();

    int getRoute53BindRebindRetries();

    int getRoute53BindingRetryIntervalMs();

    String getExperimental(String name);
}

