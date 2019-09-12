package com.pseudocode.cloud.eurekaserver;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.pseudocode.netflix.eureka.core.EurekaServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.PropertyResolver;

@ConfigurationProperties(EurekaServerConfigBean.PREFIX)
public class EurekaServerConfigBean implements EurekaServerConfig {

    public static final String PREFIX = "eureka.server";

    private static final int MINUTES = 60 * 1000;

    @Autowired(required = false)
    PropertyResolver propertyResolver;

    //获取aws访问的id，主要用于弹性ip绑定，此配置是用于aws上的，默认为null
    private String aWSAccessId;

    //获取aws私有秘钥，主要用于弹性ip绑定，此配置是用于aws上的，默认为null
    private String aWSSecretKey;

    //获取服务器尝试绑定到候选的EIP的次数，默认为3
    private int eIPBindRebindRetries = 3;

    //稳定ip绑定状态检查，默认为5 * 60 * 1000
    private int eIPBindingRetryIntervalMs = 5 * MINUTES;

    //服务器检查ip绑定的时间间隔，单位为毫秒，默认为1 * 60 * 1000
    private int eIPBindingRetryIntervalMsWhenUnbound = 1 * MINUTES;

    //是否开启自我保护机制
    private boolean enableSelfPreservation = true;

    //开启自我保护模式比例，超过该比例后开启自我保护模式
    private double renewalPercentThreshold = 0.85;

    //自我保护模式比例更新定时任务执行频率，单位：毫秒
    private int renewalThresholdUpdateIntervalMs = 15 * MINUTES;

    //集群里eureka节点的变化信息更新的时间间隔，单位为毫秒，默认为10 * 60 * 1000
    private int peerEurekaNodesUpdateIntervalMs = 10 * MINUTES;

    //集群里服务器尝试复制数据的次数，默认为5
    private int numberOfReplicationRetries = 5;

    //服务器节点的状态信息被更新的时间间隔，单位为毫秒，默认为30 * 1000
    private int peerEurekaStatusRefreshTimeIntervalMs = 30 * 1000;

    //在Eureka服务器获取不到集群里对等服务器上的实例时，需要等待的时间，单位为毫秒，默认为1000 * 60 * 5
    private int waitTimeInMsWhenSyncEmpty = 5 * MINUTES;

    //连接对等节点服务器复制的超时的时间，单位为毫秒，默认为200
    private int peerNodeConnectTimeoutMs = 200;

    //读取对等节点服务器复制的超时的时间，单位为毫秒，默认为200
    private int peerNodeReadTimeoutMs = 200;

    //获取对等节点上http连接的总数，默认为1000
    private int peerNodeTotalConnections = 1000;

    //获取每个路由下对等节点上http连接的总数，默认为500
    private int peerNodeTotalConnectionsPerHost = 500;

    //对等节点http连接被清理之后服务器的空闲时间，默认为30秒
    private int peerNodeConnectionIdleTimeoutSeconds = 30;

    //客户端保持增量信息缓存的时间，从而保证不会丢失这些信息，单位为毫秒，默认为3 * 60 * 1000
    private long retentionTimeInMSInDeltaQueue = 3 * MINUTES;

    //清理任务程序被唤醒的时间间隔，清理过期的增量信息，单位为毫秒，默认为30 * 1000
    private long deltaRetentionTimerIntervalInMs = 30 * 1000;

    //过期实例应该启动并运行的时间间隔，单位为毫秒，默认为60 * 1000
    private long evictionIntervalTimerInMs = 60 * 1000;

    //查询AWS上ASG（自动缩放组）信息的超时值，单位为毫秒，默认为300
    private int aSGQueryTimeoutMs = 300;

    //从AWS上更新ASG信息的时间间隔，单位为毫秒，默认为5 * 60 * 1000
    private long aSGUpdateIntervalMs = 5 * MINUTES;

    //缓存ASG信息的到期时间，单位为毫秒，默认为10 * 60 * 1000
    private long aSGCacheExpiryTimeoutMs = 10 * MINUTES; // defaults to longer than the
    // asg update interval

    //当注册表信息被改变时，则其被保存在缓存中不失效的时间，默认为180秒
    private long responseCacheAutoExpirationInSeconds = 180;

    //响应缓存更新的时间间隔，默认为30 * 1000毫秒
    private long responseCacheUpdateIntervalMs = 30 * 1000;

    //是否使用只读缓存
    private boolean useReadOnlyResponseCache = true;

    //增量信息是否可以提供给客户端看，默认为false
    private boolean disableDelta;

    //状态复制线程可以保持存活的空闲时间，默认为10分钟
    private long maxIdleThreadInMinutesAgeForStatusReplication = 10;

    //被用于状态复制的线程的最小数目，默认为1
    private int minThreadsForStatusReplication = 1;

    //被用于状态复制的线程的最大数目，默认为1
    private int maxThreadsForStatusReplication = 1;

    //可允许的状态复制池备份复制事件的最大数量，默认为10000
    private int maxElementsInStatusReplicationPool = 10000;

    //当时间变化实例是否跟着同步，默认为true
    private boolean syncWhenTimestampDiffers = true;

    //当eureka服务器启动时尝试去获取集群里其他服务器上的注册信息的次数
    private int registrySyncRetries = 0;

    //当eureka服务器启动时获取其他服务器的注册信息失败时，会再次尝试获取，期间需要等待的时间，默认为30 * 1000毫秒
    private long registrySyncRetryWaitMs = 30 * 1000;

    //复制池备份复制事件的最大数量，默认为10000
    private int maxElementsInPeerReplicationPool = 10000;

    //复制线程可以保持存活的空闲时间，默认为15分钟
    private long maxIdleThreadAgeInMinutesForPeerReplication = 15;

    //用于复制线程的最小数目，默认为5
    private int minThreadsForPeerReplication = 5;

    //用于复制线程的最大数目，默认为20
    private int maxThreadsForPeerReplication = 20;

    //尝试在丢弃复制事件之前进行复制的时间，默认为30000毫秒
    private int maxTimeForReplication = 30000;

    //对集群中服务器节点的连接是否应该准备，默认为true
    private boolean primeAwsReplicaConnections = true;

    //增量信息是否可以提供给客户端或一些远程地区，默认为false
    private boolean disableDeltaForRemoteRegions;

    //连接到对等远程地eureka节点的超时时间，默认为1000毫秒
    private int remoteRegionConnectTimeoutMs = 1000;

    //获取从远程地区eureka节点读取信息的超时时间，默认为1000毫秒
    private int remoteRegionReadTimeoutMs = 1000;

    //获取远程地区对等节点上http连接的总数，默认为1000
    private int remoteRegionTotalConnections = 1000;

    //获取远程地区每个网关的对等节点上http连接的总数，默认为500
    private int remoteRegionTotalConnectionsPerHost = 500;

    //http连接被清理之后远程地区服务器的空闲时间，默认为30秒
    private int remoteRegionConnectionIdleTimeoutSeconds = 30;

    //eureka服务器中获取的内容是否在远程地区被压缩，默认为true
    private boolean gZipContentFromRemoteRegion = true;

    //针对远程地区发现的网址域名的map
    private Map<String, String> remoteRegionUrlsWithName = new HashMap<>();

    //远程地区的URL列表
    private String[] remoteRegionUrls;

    //通过远程区域中检索的应用程序的列表
    private Map<String, Set<String>> remoteRegionAppWhitelist;

    //从远程区域取出该注册表的信息的时间间隔，默认为30秒
    private int remoteRegionRegistryFetchInterval = 30;

    //用于执行远程区域注册表请求的线程池的大小，默认为20
    private int remoteRegionFetchThreadPoolSize = 20;

    //用来合格请求远程区域注册表的信任存储文件，默认为空
    private String remoteRegionTrustStore = "";

    //获取偏远地区信任存储文件的密码，默认为“changeit”
    private String remoteRegionTrustStorePassword = "changeit";

    //如果在远程区域本地没有实例运行，对于应用程序回退的旧行为是否被禁用， 默认为false
    private boolean disableTransparentFallbackToOtherRegion;

    //表示集群节点之间的复制是否为了网络效率而进行批处理，默认为false
    private boolean batchReplication;

    //请求限流是否开启
    private boolean rateLimiterEnabled = false;

    private boolean rateLimiterThrottleStandardClients = false;

    private Set<String> rateLimiterPrivilegedClients = Collections.emptySet();

    private int rateLimiterBurstSize = 10;

    private int rateLimiterRegistryFetchAverageRate = 500;

    private int rateLimiterFullFetchAverageRate = 100;

    private boolean logIdentityHeaders = true;

    //用来描述从AWS第三账户的自动缩放组中的角色名称，默认为“ListAutoScalingGroups”
    private String listAutoScalingGroupsRoleName = "ListAutoScalingGroups";

    //复制的数据在发送请求时是否被压缩，默认为false
    private boolean enableReplicatedRequestCompression = false;

    //如果没有设置默认的编解码器将使用全JSON编解码器，获取的是编码器的类名称
    private String jsonCodecName;

    //如果没有设置默认的编解码器将使用xml编解码器，获取的是编码器的类名称
    private String xmlCodecName;

    //服务器尝试绑定到候选Route53域的次数，默认为3
    private int route53BindRebindRetries = 3;

    //服务器应该检查是否和Route53域绑定的时间间隔，默认为5 * 60 * 1000毫秒
    private int route53BindingRetryIntervalMs = 5 * MINUTES;

    //用于建立route53域的tt
    private long route53DomainTTL = 30;

    //获取配置绑定EIP或Route53的策略
    //private AwsBindingStrategy bindingStrategy = AwsBindingStrategy.EIP;

    private int minAvailableInstancesForPeerReplication = -1;

    @Override
    public boolean shouldEnableSelfPreservation() {
        return this.enableSelfPreservation;
    }

    @Override
    public boolean shouldDisableDelta() {
        return this.disableDelta;
    }

    @Override
    public boolean shouldSyncWhenTimestampDiffers() {
        return this.syncWhenTimestampDiffers;
    }

    @Override
    public boolean shouldPrimeAwsReplicaConnections() {
        return this.primeAwsReplicaConnections;
    }

    @Override
    public boolean shouldDisableDeltaForRemoteRegions() {
        return this.disableDeltaForRemoteRegions;
    }

    @Override
    public boolean shouldGZipContentFromRemoteRegion() {
        return this.gZipContentFromRemoteRegion;
    }

    @Override
    public Set<String> getRemoteRegionAppWhitelist(String regionName) {
        return this.remoteRegionAppWhitelist.get(regionName == null ? "global" : regionName.trim().toLowerCase());
    }

    @Override
    public boolean disableTransparentFallbackToOtherRegion() {
        return this.disableTransparentFallbackToOtherRegion;
    }

    @Override
    public boolean shouldBatchReplication() {
        return this.batchReplication;
    }

    @Override
    public boolean shouldLogIdentityHeaders() {
        return this.logIdentityHeaders;
    }

    @Override
    public String getJsonCodecName() {
        return this.jsonCodecName;
    }

    @Override
    public String getXmlCodecName() {
        return this.xmlCodecName;
    }

    @Override
    public boolean shouldUseReadOnlyResponseCache() {
        return this.useReadOnlyResponseCache;
    }

    @Override
    public boolean shouldEnableReplicatedRequestCompression() {
        return this.enableReplicatedRequestCompression;
    }

    @Override
    public String getExperimental(String name) {
        if (this.propertyResolver != null) {
            return this.propertyResolver.getProperty(PREFIX + ".experimental." + name, String.class, null);
        }
        return null;
    }

    @Override
    public int getHealthStatusMinNumberOfAvailablePeers() {
        return this.minAvailableInstancesForPeerReplication;
    }

    public PropertyResolver getPropertyResolver() {
        return propertyResolver;
    }

    public void setPropertyResolver(PropertyResolver propertyResolver) {
        this.propertyResolver = propertyResolver;
    }

    public String getAWSAccessId() {
        return aWSAccessId;
    }

    public void setAWSAccessId(String aWSAccessId) {
        this.aWSAccessId = aWSAccessId;
    }

    public String getAWSSecretKey() {
        return aWSSecretKey;
    }

    public void setAWSSecretKey(String aWSSecretKey) {
        this.aWSSecretKey = aWSSecretKey;
    }

    public int getEIPBindRebindRetries() {
        return eIPBindRebindRetries;
    }

    public void setEIPBindRebindRetries(int eIPBindRebindRetries) {
        this.eIPBindRebindRetries = eIPBindRebindRetries;
    }

    public int getEIPBindingRetryIntervalMs() {
        return eIPBindingRetryIntervalMs;
    }

    public void setEIPBindingRetryIntervalMs(int eIPBindingRetryIntervalMs) {
        this.eIPBindingRetryIntervalMs = eIPBindingRetryIntervalMs;
    }

    public int getEIPBindingRetryIntervalMsWhenUnbound() {
        return eIPBindingRetryIntervalMsWhenUnbound;
    }

    public void setEIPBindingRetryIntervalMsWhenUnbound(int eIPBindingRetryIntervalMsWhenUnbound) {
        this.eIPBindingRetryIntervalMsWhenUnbound = eIPBindingRetryIntervalMsWhenUnbound;
    }

    public boolean isEnableSelfPreservation() {
        return enableSelfPreservation;
    }

    public void setEnableSelfPreservation(boolean enableSelfPreservation) {
        this.enableSelfPreservation = enableSelfPreservation;
    }

    @Override
    public double getRenewalPercentThreshold() {
        return renewalPercentThreshold;
    }

    public void setRenewalPercentThreshold(double renewalPercentThreshold) {
        this.renewalPercentThreshold = renewalPercentThreshold;
    }

    @Override
    public int getRenewalThresholdUpdateIntervalMs() {
        return renewalThresholdUpdateIntervalMs;
    }

    public void setRenewalThresholdUpdateIntervalMs(int renewalThresholdUpdateIntervalMs) {
        this.renewalThresholdUpdateIntervalMs = renewalThresholdUpdateIntervalMs;
    }

    @Override
    public int getPeerEurekaNodesUpdateIntervalMs() {
        return peerEurekaNodesUpdateIntervalMs;
    }

    public void setPeerEurekaNodesUpdateIntervalMs(int peerEurekaNodesUpdateIntervalMs) {
        this.peerEurekaNodesUpdateIntervalMs = peerEurekaNodesUpdateIntervalMs;
    }

    @Override
    public int getNumberOfReplicationRetries() {
        return numberOfReplicationRetries;
    }

    public void setNumberOfReplicationRetries(int numberOfReplicationRetries) {
        this.numberOfReplicationRetries = numberOfReplicationRetries;
    }

    @Override
    public int getPeerEurekaStatusRefreshTimeIntervalMs() {
        return peerEurekaStatusRefreshTimeIntervalMs;
    }

    public void setPeerEurekaStatusRefreshTimeIntervalMs(int peerEurekaStatusRefreshTimeIntervalMs) {
        this.peerEurekaStatusRefreshTimeIntervalMs = peerEurekaStatusRefreshTimeIntervalMs;
    }

    @Override
    public int getWaitTimeInMsWhenSyncEmpty() {
        return waitTimeInMsWhenSyncEmpty;
    }

    public void setWaitTimeInMsWhenSyncEmpty(int waitTimeInMsWhenSyncEmpty) {
        this.waitTimeInMsWhenSyncEmpty = waitTimeInMsWhenSyncEmpty;
    }

    @Override
    public int getPeerNodeConnectTimeoutMs() {
        return peerNodeConnectTimeoutMs;
    }

    public void setPeerNodeConnectTimeoutMs(int peerNodeConnectTimeoutMs) {
        this.peerNodeConnectTimeoutMs = peerNodeConnectTimeoutMs;
    }

    @Override
    public int getPeerNodeReadTimeoutMs() {
        return peerNodeReadTimeoutMs;
    }

    public void setPeerNodeReadTimeoutMs(int peerNodeReadTimeoutMs) {
        this.peerNodeReadTimeoutMs = peerNodeReadTimeoutMs;
    }

    @Override
    public int getPeerNodeTotalConnections() {
        return peerNodeTotalConnections;
    }

    public void setPeerNodeTotalConnections(int peerNodeTotalConnections) {
        this.peerNodeTotalConnections = peerNodeTotalConnections;
    }

    @Override
    public int getPeerNodeTotalConnectionsPerHost() {
        return peerNodeTotalConnectionsPerHost;
    }

    public void setPeerNodeTotalConnectionsPerHost(int peerNodeTotalConnectionsPerHost) {
        this.peerNodeTotalConnectionsPerHost = peerNodeTotalConnectionsPerHost;
    }

    @Override
    public int getPeerNodeConnectionIdleTimeoutSeconds() {
        return peerNodeConnectionIdleTimeoutSeconds;
    }

    public void setPeerNodeConnectionIdleTimeoutSeconds(int peerNodeConnectionIdleTimeoutSeconds) {
        this.peerNodeConnectionIdleTimeoutSeconds = peerNodeConnectionIdleTimeoutSeconds;
    }

    @Override
    public long getRetentionTimeInMSInDeltaQueue() {
        return retentionTimeInMSInDeltaQueue;
    }

    public void setRetentionTimeInMSInDeltaQueue(long retentionTimeInMSInDeltaQueue) {
        this.retentionTimeInMSInDeltaQueue = retentionTimeInMSInDeltaQueue;
    }

    @Override
    public long getDeltaRetentionTimerIntervalInMs() {
        return deltaRetentionTimerIntervalInMs;
    }

    public void setDeltaRetentionTimerIntervalInMs(long deltaRetentionTimerIntervalInMs) {
        this.deltaRetentionTimerIntervalInMs = deltaRetentionTimerIntervalInMs;
    }

    @Override
    public long getEvictionIntervalTimerInMs() {
        return evictionIntervalTimerInMs;
    }

    public void setEvictionIntervalTimerInMs(long evictionIntervalTimerInMs) {
        this.evictionIntervalTimerInMs = evictionIntervalTimerInMs;
    }

    public int getASGQueryTimeoutMs() {
        return aSGQueryTimeoutMs;
    }

    public void setASGQueryTimeoutMs(int aSGQueryTimeoutMs) {
        this.aSGQueryTimeoutMs = aSGQueryTimeoutMs;
    }

    public long getASGUpdateIntervalMs() {
        return aSGUpdateIntervalMs;
    }

    public void setASGUpdateIntervalMs(long aSGUpdateIntervalMs) {
        this.aSGUpdateIntervalMs = aSGUpdateIntervalMs;
    }

    public long getASGCacheExpiryTimeoutMs() {
        return aSGCacheExpiryTimeoutMs;
    }

    public void setASGCacheExpiryTimeoutMs(long aSGCacheExpiryTimeoutMs) {
        this.aSGCacheExpiryTimeoutMs = aSGCacheExpiryTimeoutMs;
    }

    @Override
    public long getResponseCacheAutoExpirationInSeconds() {
        return responseCacheAutoExpirationInSeconds;
    }

    public void setResponseCacheAutoExpirationInSeconds(long responseCacheAutoExpirationInSeconds) {
        this.responseCacheAutoExpirationInSeconds = responseCacheAutoExpirationInSeconds;
    }

    @Override
    public long getResponseCacheUpdateIntervalMs() {
        return responseCacheUpdateIntervalMs;
    }

    public void setResponseCacheUpdateIntervalMs(long responseCacheUpdateIntervalMs) {
        this.responseCacheUpdateIntervalMs = responseCacheUpdateIntervalMs;
    }

    public boolean isUseReadOnlyResponseCache() {
        return useReadOnlyResponseCache;
    }

    public void setUseReadOnlyResponseCache(boolean useReadOnlyResponseCache) {
        this.useReadOnlyResponseCache = useReadOnlyResponseCache;
    }

    public boolean isDisableDelta() {
        return disableDelta;
    }

    public void setDisableDelta(boolean disableDelta) {
        this.disableDelta = disableDelta;
    }

    @Override
    public long getMaxIdleThreadInMinutesAgeForStatusReplication() {
        return maxIdleThreadInMinutesAgeForStatusReplication;
    }

    public void setMaxIdleThreadInMinutesAgeForStatusReplication(long maxIdleThreadInMinutesAgeForStatusReplication) {
        this.maxIdleThreadInMinutesAgeForStatusReplication = maxIdleThreadInMinutesAgeForStatusReplication;
    }

    @Override
    public int getMinThreadsForStatusReplication() {
        return minThreadsForStatusReplication;
    }

    public void setMinThreadsForStatusReplication(int minThreadsForStatusReplication) {
        this.minThreadsForStatusReplication = minThreadsForStatusReplication;
    }

    @Override
    public int getMaxThreadsForStatusReplication() {
        return maxThreadsForStatusReplication;
    }

    public void setMaxThreadsForStatusReplication(int maxThreadsForStatusReplication) {
        this.maxThreadsForStatusReplication = maxThreadsForStatusReplication;
    }

    @Override
    public int getMaxElementsInStatusReplicationPool() {
        return maxElementsInStatusReplicationPool;
    }

    public void setMaxElementsInStatusReplicationPool(int maxElementsInStatusReplicationPool) {
        this.maxElementsInStatusReplicationPool = maxElementsInStatusReplicationPool;
    }

    public boolean isSyncWhenTimestampDiffers() {
        return syncWhenTimestampDiffers;
    }

    public void setSyncWhenTimestampDiffers(boolean syncWhenTimestampDiffers) {
        this.syncWhenTimestampDiffers = syncWhenTimestampDiffers;
    }

    @Override
    public int getRegistrySyncRetries() {
        return registrySyncRetries;
    }

    public void setRegistrySyncRetries(int registrySyncRetries) {
        this.registrySyncRetries = registrySyncRetries;
    }

    @Override
    public long getRegistrySyncRetryWaitMs() {
        return registrySyncRetryWaitMs;
    }

    public void setRegistrySyncRetryWaitMs(long registrySyncRetryWaitMs) {
        this.registrySyncRetryWaitMs = registrySyncRetryWaitMs;
    }

    @Override
    public int getMaxElementsInPeerReplicationPool() {
        return maxElementsInPeerReplicationPool;
    }

    public void setMaxElementsInPeerReplicationPool(int maxElementsInPeerReplicationPool) {
        this.maxElementsInPeerReplicationPool = maxElementsInPeerReplicationPool;
    }

    @Override
    public long getMaxIdleThreadAgeInMinutesForPeerReplication() {
        return maxIdleThreadAgeInMinutesForPeerReplication;
    }

    public void setMaxIdleThreadAgeInMinutesForPeerReplication(long maxIdleThreadAgeInMinutesForPeerReplication) {
        this.maxIdleThreadAgeInMinutesForPeerReplication = maxIdleThreadAgeInMinutesForPeerReplication;
    }

    @Override
    public int getMinThreadsForPeerReplication() {
        return minThreadsForPeerReplication;
    }

    public void setMinThreadsForPeerReplication(int minThreadsForPeerReplication) {
        this.minThreadsForPeerReplication = minThreadsForPeerReplication;
    }

    @Override
    public int getMaxThreadsForPeerReplication() {
        return maxThreadsForPeerReplication;
    }

    public void setMaxThreadsForPeerReplication(int maxThreadsForPeerReplication) {
        this.maxThreadsForPeerReplication = maxThreadsForPeerReplication;
    }

    @Override
    public int getMaxTimeForReplication() {
        return maxTimeForReplication;
    }

    public void setMaxTimeForReplication(int maxTimeForReplication) {
        this.maxTimeForReplication = maxTimeForReplication;
    }

    public boolean isPrimeAwsReplicaConnections() {
        return primeAwsReplicaConnections;
    }

    public void setPrimeAwsReplicaConnections(boolean primeAwsReplicaConnections) {
        this.primeAwsReplicaConnections = primeAwsReplicaConnections;
    }

    public boolean isDisableDeltaForRemoteRegions() {
        return disableDeltaForRemoteRegions;
    }

    public void setDisableDeltaForRemoteRegions(boolean disableDeltaForRemoteRegions) {
        this.disableDeltaForRemoteRegions = disableDeltaForRemoteRegions;
    }

    @Override
    public int getRemoteRegionConnectTimeoutMs() {
        return remoteRegionConnectTimeoutMs;
    }

    public void setRemoteRegionConnectTimeoutMs(int remoteRegionConnectTimeoutMs) {
        this.remoteRegionConnectTimeoutMs = remoteRegionConnectTimeoutMs;
    }

    @Override
    public int getRemoteRegionReadTimeoutMs() {
        return remoteRegionReadTimeoutMs;
    }

    public void setRemoteRegionReadTimeoutMs(int remoteRegionReadTimeoutMs) {
        this.remoteRegionReadTimeoutMs = remoteRegionReadTimeoutMs;
    }

    @Override
    public int getRemoteRegionTotalConnections() {
        return remoteRegionTotalConnections;
    }

    public void setRemoteRegionTotalConnections(int remoteRegionTotalConnections) {
        this.remoteRegionTotalConnections = remoteRegionTotalConnections;
    }

    @Override
    public int getRemoteRegionTotalConnectionsPerHost() {
        return remoteRegionTotalConnectionsPerHost;
    }

    public void setRemoteRegionTotalConnectionsPerHost(int remoteRegionTotalConnectionsPerHost) {
        this.remoteRegionTotalConnectionsPerHost = remoteRegionTotalConnectionsPerHost;
    }

    @Override
    public int getRemoteRegionConnectionIdleTimeoutSeconds() {
        return remoteRegionConnectionIdleTimeoutSeconds;
    }

    public void setRemoteRegionConnectionIdleTimeoutSeconds(int remoteRegionConnectionIdleTimeoutSeconds) {
        this.remoteRegionConnectionIdleTimeoutSeconds = remoteRegionConnectionIdleTimeoutSeconds;
    }

    public boolean isgZipContentFromRemoteRegion() {
        return gZipContentFromRemoteRegion;
    }

    public void setgZipContentFromRemoteRegion(boolean gZipContentFromRemoteRegion) {
        this.gZipContentFromRemoteRegion = gZipContentFromRemoteRegion;
    }

    @Override
    public Map<String, String> getRemoteRegionUrlsWithName() {
        return remoteRegionUrlsWithName;
    }

    public void setRemoteRegionUrlsWithName(Map<String, String> remoteRegionUrlsWithName) {
        this.remoteRegionUrlsWithName = remoteRegionUrlsWithName;
    }

    @Override
    public String[] getRemoteRegionUrls() {
        return remoteRegionUrls;
    }

    public void setRemoteRegionUrls(String[] remoteRegionUrls) {
        this.remoteRegionUrls = remoteRegionUrls;
    }

    public Map<String, Set<String>> getRemoteRegionAppWhitelist() {
        return remoteRegionAppWhitelist;
    }

    public void setRemoteRegionAppWhitelist(Map<String, Set<String>> remoteRegionAppWhitelist) {
        this.remoteRegionAppWhitelist = remoteRegionAppWhitelist;
    }

    @Override
    public int getRemoteRegionRegistryFetchInterval() {
        return remoteRegionRegistryFetchInterval;
    }

    public void setRemoteRegionRegistryFetchInterval(int remoteRegionRegistryFetchInterval) {
        this.remoteRegionRegistryFetchInterval = remoteRegionRegistryFetchInterval;
    }

    @Override
    public int getRemoteRegionFetchThreadPoolSize() {
        return remoteRegionFetchThreadPoolSize;
    }

    public void setRemoteRegionFetchThreadPoolSize(int remoteRegionFetchThreadPoolSize) {
        this.remoteRegionFetchThreadPoolSize = remoteRegionFetchThreadPoolSize;
    }

    @Override
    public String getRemoteRegionTrustStore() {
        return remoteRegionTrustStore;
    }

    public void setRemoteRegionTrustStore(String remoteRegionTrustStore) {
        this.remoteRegionTrustStore = remoteRegionTrustStore;
    }

    @Override
    public String getRemoteRegionTrustStorePassword() {
        return remoteRegionTrustStorePassword;
    }

    public void setRemoteRegionTrustStorePassword(String remoteRegionTrustStorePassword) {
        this.remoteRegionTrustStorePassword = remoteRegionTrustStorePassword;
    }

    public boolean isDisableTransparentFallbackToOtherRegion() {
        return disableTransparentFallbackToOtherRegion;
    }

    public void setDisableTransparentFallbackToOtherRegion(boolean disableTransparentFallbackToOtherRegion) {
        this.disableTransparentFallbackToOtherRegion = disableTransparentFallbackToOtherRegion;
    }

    public boolean isBatchReplication() {
        return batchReplication;
    }

    public void setBatchReplication(boolean batchReplication) {
        this.batchReplication = batchReplication;
    }

    @Override
    public boolean isRateLimiterEnabled() {
        return rateLimiterEnabled;
    }

    public void setRateLimiterEnabled(boolean rateLimiterEnabled) {
        this.rateLimiterEnabled = rateLimiterEnabled;
    }

    @Override
    public boolean isRateLimiterThrottleStandardClients() {
        return rateLimiterThrottleStandardClients;
    }

    public void setRateLimiterThrottleStandardClients(boolean rateLimiterThrottleStandardClients) {
        this.rateLimiterThrottleStandardClients = rateLimiterThrottleStandardClients;
    }

    @Override
    public Set<String> getRateLimiterPrivilegedClients() {
        return rateLimiterPrivilegedClients;
    }

    public void setRateLimiterPrivilegedClients(Set<String> rateLimiterPrivilegedClients) {
        this.rateLimiterPrivilegedClients = rateLimiterPrivilegedClients;
    }

    @Override
    public int getRateLimiterBurstSize() {
        return rateLimiterBurstSize;
    }

    public void setRateLimiterBurstSize(int rateLimiterBurstSize) {
        this.rateLimiterBurstSize = rateLimiterBurstSize;
    }

    @Override
    public int getRateLimiterRegistryFetchAverageRate() {
        return rateLimiterRegistryFetchAverageRate;
    }

    public void setRateLimiterRegistryFetchAverageRate(int rateLimiterRegistryFetchAverageRate) {
        this.rateLimiterRegistryFetchAverageRate = rateLimiterRegistryFetchAverageRate;
    }

    @Override
    public int getRateLimiterFullFetchAverageRate() {
        return rateLimiterFullFetchAverageRate;
    }

    public void setRateLimiterFullFetchAverageRate(int rateLimiterFullFetchAverageRate) {
        this.rateLimiterFullFetchAverageRate = rateLimiterFullFetchAverageRate;
    }

    public boolean isLogIdentityHeaders() {
        return logIdentityHeaders;
    }

    public void setLogIdentityHeaders(boolean logIdentityHeaders) {
        this.logIdentityHeaders = logIdentityHeaders;
    }

    @Override
    public String getListAutoScalingGroupsRoleName() {
        return listAutoScalingGroupsRoleName;
    }

    public void setListAutoScalingGroupsRoleName(String listAutoScalingGroupsRoleName) {
        this.listAutoScalingGroupsRoleName = listAutoScalingGroupsRoleName;
    }

    public boolean isEnableReplicatedRequestCompression() {
        return enableReplicatedRequestCompression;
    }

    public void setEnableReplicatedRequestCompression(boolean enableReplicatedRequestCompression) {
        this.enableReplicatedRequestCompression = enableReplicatedRequestCompression;
    }

    public void setJsonCodecName(String jsonCodecName) {
        this.jsonCodecName = jsonCodecName;
    }

    public void setXmlCodecName(String xmlCodecName) {
        this.xmlCodecName = xmlCodecName;
    }

    @Override
    public int getRoute53BindRebindRetries() {
        return route53BindRebindRetries;
    }

    public void setRoute53BindRebindRetries(int route53BindRebindRetries) {
        this.route53BindRebindRetries = route53BindRebindRetries;
    }

    @Override
    public int getRoute53BindingRetryIntervalMs() {
        return route53BindingRetryIntervalMs;
    }

    public void setRoute53BindingRetryIntervalMs(int route53BindingRetryIntervalMs) {
        this.route53BindingRetryIntervalMs = route53BindingRetryIntervalMs;
    }

    @Override
    public long getRoute53DomainTTL() {
        return route53DomainTTL;
    }

    public void setRoute53DomainTTL(long route53DomainTTL) {
        this.route53DomainTTL = route53DomainTTL;
    }
//
//    @Override
//    public AwsBindingStrategy getBindingStrategy() {
//        return bindingStrategy;
//    }
//
//    public void setBindingStrategy(AwsBindingStrategy bindingStrategy) {
//        this.bindingStrategy = bindingStrategy;
//    }

    public int getMinAvailableInstancesForPeerReplication() {
        return minAvailableInstancesForPeerReplication;
    }

    public void setMinAvailableInstancesForPeerReplication(int minAvailableInstancesForPeerReplication) {
        this.minAvailableInstancesForPeerReplication = minAvailableInstancesForPeerReplication;
    }

}

