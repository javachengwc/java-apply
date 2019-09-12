package com.pseudocode.cloud.eurekaclient;

import com.pseudocode.netflix.eureka.client.appinfo.EurekaAccept;
import com.pseudocode.netflix.eureka.client.discovery.EurekaClientConfig;
import com.pseudocode.netflix.eureka.client.discovery.shared.transport.EurekaTransportConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.core.env.PropertyResolver;
import org.springframework.util.StringUtils;

import java.util.*;

@ConfigurationProperties(EurekaClientConfigBean.PREFIX)
public class EurekaClientConfigBean implements EurekaClientConfig {

    public static final String PREFIX = "eureka.client";

    public static final String DEFAULT_PREFIX = "/eureka";

    public static final String DEFAULT_URL = "http://localhost:8761" + DEFAULT_PREFIX+ "/";

    public static final String DEFAULT_ZONE = "defaultZone";

    private static final int MINUTES = 60;

    @Autowired(required = false)
    PropertyResolver propertyResolver;

    private boolean enabled = true;

    @NestedConfigurationProperty
    //private EurekaTransportConfig transport = new CloudEurekaTransportConfig();
    private EurekaTransportConfig transport = null;

    //从 Eureka-Server 拉取注册信息频率，单位：秒。默认：30 秒
    private int registryFetchIntervalSeconds = 30;

    //向 Eureka-Server 同步应用实例信息变化频率，单位：秒
    private int instanceInfoReplicationIntervalSeconds = 30;

    //向 Eureka-Server 同步应用信息变化初始化延迟，单位：秒
    private int initialInstanceInfoReplicationIntervalSeconds = 40;

    //轮询获取 Eureka-Server 地址变更频率，单位：秒。默认：300 秒
    private int eurekaServiceUrlPollIntervalSeconds = 5 * MINUTES;

    private String proxyPort;

    private String proxyHost;

    private String proxyUserName;

    private String proxyPassword;

    //读取超时时间
    private int eurekaServerReadTimeoutSeconds = 8;

    //连接超时时间
    private int eurekaServerConnectTimeoutSeconds = 5;

    //获取备份注册中心实现类
    //当 Eureka-Client 启动时，无法从 Eureka-Server 读取注册信息（可能挂了），从备份注册中心读取注册信息。
    // 目前 Eureka-Client 未提供合适的实现
    private String backupRegistryImpl;

    //eureka客户端允许所有eureka服务器连接的总数目
    private int eurekaServerTotalConnections = 200;

    //eureka客户端允许eureka服务器主机连接的总数目
    private int eurekaServerTotalConnectionsPerHost = 50;

    //Eureka-Server 的 URL Context
    private String eurekaServerURLContext;

    //Eureka-Server 的端口
    private String eurekaServerPort;

    private String eurekaServerDNSName;

    private String region = "us-east-1";

    //Eureka服务的http请求连接的空闲超时时间，默认为30 秒
    private int eurekaConnectionIdleTimeoutSeconds = 30;

    //此客户端只对一个单一的VIP注册表的信息感兴趣。默认为null
    private String registryRefreshSingleVipAddress;

    //心跳执行线程池大小
    private int heartbeatExecutorThreadPoolSize = 2;

    private int heartbeatExecutorExponentialBackOffBound = 10;

    //注册信息缓存刷新线程池大小
    private int cacheRefreshExecutorThreadPoolSize = 2;

    private int cacheRefreshExecutorExponentialBackOffBound = 10;

    private Map<String, String> serviceUrl = new HashMap<>();

    {
        this.serviceUrl.put(DEFAULT_ZONE, DEFAULT_URL);
    }

    //eureka注册表的内容是否被压缩，默认为true
    private boolean gZipContent = true;

    //eureka客户端是否应该使用DNS机制来获取eureka服务器的地址列表，默认为false
    private boolean useDnsForFetchingServiceUrls = false;

    //是否向 Eureka-Server 注册自身服务
    private boolean registerWithEureka = true;

    //优先使用相同区( zone )的 Eureka-Server
    private boolean preferSameZoneEureka = true;

    //是否记录eureka服务器和客户端之间在注册表的信息方面的差异，默认为false
    private boolean logDeltaDiff;

    private boolean disableDelta;

    private String fetchRemoteRegionsRegistry;

    private Map<String, String> availabilityZones = new HashMap<>();

    //是否过滤，只获取状态为开启( Up )的应用实例集合
    private boolean filterOnlyUpInstances = true;

    //是否从 Eureka-Server 拉取注册信息
    private boolean fetchRegistry = true;

    //eureka服务器序列化/反序列化的信息中获取“$”符号的的替换字符串。默认为“_-”
    private String dollarReplacement = "_-";

    //eureka服务器序列化/反序列化的信息中获取“_”符号的的替换字符串。默认为“__”
    private String escapeCharReplacement = "__";

    //服务器是否能够重定向客户端请求到备份服务器。
    //如果设置为false，服务器将直接处理请求，如果设置为true，它可能发送HTTP重定向到客户端。默认为false
    private boolean allowRedirects = false;

    //如果设置为true,客户端的状态更新将会点播更新到远程服务器上，默认为true
    private boolean onDemandUpdateStatusChange = true;

    //短暂的编码器的配置，默认为null
    private String encoderName;

    //短暂的解码器的配置，默认为null
    private String decoderName;

    private String clientDataAccept = EurekaAccept.full.name();

    //应用实例开启关闭时下线开关。默认为 tru
    private boolean shouldUnregisterOnShutdown = true;

    //应用实例开启注册开关。默认为 false,通过eureka.registration.enabled配置
    private boolean shouldEnforceRegistrationAtInit = false;

    @Override
    public boolean shouldGZipContent() {
        return this.gZipContent;
    }

    @Override
    public boolean shouldUseDnsForFetchingServiceUrls() {
        return this.useDnsForFetchingServiceUrls;
    }

    @Override
    public boolean shouldRegisterWithEureka() {
        return this.registerWithEureka;
    }

    @Override
    public boolean shouldPreferSameZoneEureka() {
        return this.preferSameZoneEureka;
    }

    @Override
    public boolean shouldLogDeltaDiff() {
        return this.logDeltaDiff;
    }

    @Override
    public boolean shouldDisableDelta() {
        return this.disableDelta;
    }

    @Override
    public boolean shouldUnregisterOnShutdown() {
        return this.shouldUnregisterOnShutdown;
    }

    @Override
    public boolean shouldEnforceRegistrationAtInit() {
        return this.shouldEnforceRegistrationAtInit;
    }

    @Override
    public String fetchRegistryForRemoteRegions() {
        return this.fetchRemoteRegionsRegistry;
    }

    @Override
    public String[] getAvailabilityZones(String region) {
        String value = this.availabilityZones.get(region);
        if (value == null) {
            value = DEFAULT_ZONE;
        }
        return value.split(",");
    }

    @Override
    public List<String> getEurekaServerServiceUrls(String myZone) {
        String serviceUrls = this.serviceUrl.get(myZone);
        if (serviceUrls == null || serviceUrls.isEmpty()) {
            serviceUrls = this.serviceUrl.get(DEFAULT_ZONE);
        }
        if (!StringUtils.isEmpty(serviceUrls)) {
            final String[] serviceUrlsSplit = StringUtils.commaDelimitedListToStringArray(serviceUrls);
            List<String> eurekaServiceUrls = new ArrayList<>(serviceUrlsSplit.length);
            for (String eurekaServiceUrl : serviceUrlsSplit) {
                if (!endsWithSlash(eurekaServiceUrl)) {
                    eurekaServiceUrl += "/";
                }
                eurekaServiceUrls.add(eurekaServiceUrl);
            }
            return eurekaServiceUrls;
        }

        return new ArrayList<>();
    }

    private boolean endsWithSlash(String url) {
        return url.endsWith("/");
    }

    @Override
    public boolean shouldFilterOnlyUpInstances() {
        return this.filterOnlyUpInstances;
    }

    @Override
    public boolean shouldFetchRegistry() {
        return this.fetchRegistry;
    }

    @Override
    public boolean allowRedirects() {
        return this.allowRedirects;
    }

    @Override
    public boolean shouldOnDemandUpdateStatusChange() {
        return this.onDemandUpdateStatusChange;
    }

    @Override
    public String getExperimental(String name) {
        if (this.propertyResolver != null) {
            return this.propertyResolver.getProperty(PREFIX + ".experimental." + name,
                    String.class, null);
        }
        return null;
    }

    @Override
    public EurekaTransportConfig getTransportConfig() {
        return getTransport();
    }

    public PropertyResolver getPropertyResolver() {
        return propertyResolver;
    }

    public void setPropertyResolver(PropertyResolver propertyResolver) {
        this.propertyResolver = propertyResolver;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public EurekaTransportConfig getTransport() {
        return transport;
    }

    public void setTransport(EurekaTransportConfig transport) {
        this.transport = transport;
    }

    @Override
    public int getRegistryFetchIntervalSeconds() {
        return registryFetchIntervalSeconds;
    }

    public void setRegistryFetchIntervalSeconds(int registryFetchIntervalSeconds) {
        this.registryFetchIntervalSeconds = registryFetchIntervalSeconds;
    }

    @Override
    public int getInstanceInfoReplicationIntervalSeconds() {
        return instanceInfoReplicationIntervalSeconds;
    }

    public void setInstanceInfoReplicationIntervalSeconds(
            int instanceInfoReplicationIntervalSeconds) {
        this.instanceInfoReplicationIntervalSeconds = instanceInfoReplicationIntervalSeconds;
    }

    @Override
    public int getInitialInstanceInfoReplicationIntervalSeconds() {
        return initialInstanceInfoReplicationIntervalSeconds;
    }

    public void setInitialInstanceInfoReplicationIntervalSeconds(
            int initialInstanceInfoReplicationIntervalSeconds) {
        this.initialInstanceInfoReplicationIntervalSeconds = initialInstanceInfoReplicationIntervalSeconds;
    }

    @Override
    public int getEurekaServiceUrlPollIntervalSeconds() {
        return eurekaServiceUrlPollIntervalSeconds;
    }

    public void setEurekaServiceUrlPollIntervalSeconds(
            int eurekaServiceUrlPollIntervalSeconds) {
        this.eurekaServiceUrlPollIntervalSeconds = eurekaServiceUrlPollIntervalSeconds;
    }

    @Override
    public String getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    @Override
    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    @Override
    public String getProxyUserName() {
        return proxyUserName;
    }

    public void setProxyUserName(String proxyUserName) {
        this.proxyUserName = proxyUserName;
    }

    @Override
    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    @Override
    public int getEurekaServerReadTimeoutSeconds() {
        return eurekaServerReadTimeoutSeconds;
    }

    public void setEurekaServerReadTimeoutSeconds(int eurekaServerReadTimeoutSeconds) {
        this.eurekaServerReadTimeoutSeconds = eurekaServerReadTimeoutSeconds;
    }

    @Override
    public int getEurekaServerConnectTimeoutSeconds() {
        return eurekaServerConnectTimeoutSeconds;
    }

    public void setEurekaServerConnectTimeoutSeconds(
            int eurekaServerConnectTimeoutSeconds) {
        this.eurekaServerConnectTimeoutSeconds = eurekaServerConnectTimeoutSeconds;
    }

    @Override
    public String getBackupRegistryImpl() {
        return backupRegistryImpl;
    }

    public void setBackupRegistryImpl(String backupRegistryImpl) {
        this.backupRegistryImpl = backupRegistryImpl;
    }

    @Override
    public int getEurekaServerTotalConnections() {
        return eurekaServerTotalConnections;
    }

    public void setEurekaServerTotalConnections(int eurekaServerTotalConnections) {
        this.eurekaServerTotalConnections = eurekaServerTotalConnections;
    }

    @Override
    public int getEurekaServerTotalConnectionsPerHost() {
        return eurekaServerTotalConnectionsPerHost;
    }

    public void setEurekaServerTotalConnectionsPerHost(
            int eurekaServerTotalConnectionsPerHost) {
        this.eurekaServerTotalConnectionsPerHost = eurekaServerTotalConnectionsPerHost;
    }

    @Override
    public String getEurekaServerURLContext() {
        return eurekaServerURLContext;
    }

    public void setEurekaServerURLContext(String eurekaServerURLContext) {
        this.eurekaServerURLContext = eurekaServerURLContext;
    }

    @Override
    public String getEurekaServerPort() {
        return eurekaServerPort;
    }

    public void setEurekaServerPort(String eurekaServerPort) {
        this.eurekaServerPort = eurekaServerPort;
    }

    @Override
    public String getEurekaServerDNSName() {
        return eurekaServerDNSName;
    }

    public void setEurekaServerDNSName(String eurekaServerDNSName) {
        this.eurekaServerDNSName = eurekaServerDNSName;
    }

    @Override
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public int getEurekaConnectionIdleTimeoutSeconds() {
        return eurekaConnectionIdleTimeoutSeconds;
    }

    public void setEurekaConnectionIdleTimeoutSeconds(
            int eurekaConnectionIdleTimeoutSeconds) {
        this.eurekaConnectionIdleTimeoutSeconds = eurekaConnectionIdleTimeoutSeconds;
    }

    @Override
    public String getRegistryRefreshSingleVipAddress() {
        return registryRefreshSingleVipAddress;
    }

    public void setRegistryRefreshSingleVipAddress(
            String registryRefreshSingleVipAddress) {
        this.registryRefreshSingleVipAddress = registryRefreshSingleVipAddress;
    }

    @Override
    public int getHeartbeatExecutorThreadPoolSize() {
        return heartbeatExecutorThreadPoolSize;
    }

    public void setHeartbeatExecutorThreadPoolSize(int heartbeatExecutorThreadPoolSize) {
        this.heartbeatExecutorThreadPoolSize = heartbeatExecutorThreadPoolSize;
    }

    @Override
    public int getHeartbeatExecutorExponentialBackOffBound() {
        return heartbeatExecutorExponentialBackOffBound;
    }

    public void setHeartbeatExecutorExponentialBackOffBound(
            int heartbeatExecutorExponentialBackOffBound) {
        this.heartbeatExecutorExponentialBackOffBound = heartbeatExecutorExponentialBackOffBound;
    }

    @Override
    public int getCacheRefreshExecutorThreadPoolSize() {
        return cacheRefreshExecutorThreadPoolSize;
    }

    public void setCacheRefreshExecutorThreadPoolSize(
            int cacheRefreshExecutorThreadPoolSize) {
        this.cacheRefreshExecutorThreadPoolSize = cacheRefreshExecutorThreadPoolSize;
    }

    @Override
    public int getCacheRefreshExecutorExponentialBackOffBound() {
        return cacheRefreshExecutorExponentialBackOffBound;
    }

    public void setCacheRefreshExecutorExponentialBackOffBound(
            int cacheRefreshExecutorExponentialBackOffBound) {
        this.cacheRefreshExecutorExponentialBackOffBound = cacheRefreshExecutorExponentialBackOffBound;
    }

    public Map<String, String> getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(Map<String, String> serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public boolean isgZipContent() {
        return gZipContent;
    }

    public void setgZipContent(boolean gZipContent) {
        this.gZipContent = gZipContent;
    }

    public boolean isUseDnsForFetchingServiceUrls() {
        return useDnsForFetchingServiceUrls;
    }

    public void setUseDnsForFetchingServiceUrls(boolean useDnsForFetchingServiceUrls) {
        this.useDnsForFetchingServiceUrls = useDnsForFetchingServiceUrls;
    }

    public boolean isRegisterWithEureka() {
        return registerWithEureka;
    }

    public void setRegisterWithEureka(boolean registerWithEureka) {
        this.registerWithEureka = registerWithEureka;
    }

    public boolean isPreferSameZoneEureka() {
        return preferSameZoneEureka;
    }

    public void setPreferSameZoneEureka(boolean preferSameZoneEureka) {
        this.preferSameZoneEureka = preferSameZoneEureka;
    }

    public boolean isLogDeltaDiff() {
        return logDeltaDiff;
    }

    public void setLogDeltaDiff(boolean logDeltaDiff) {
        this.logDeltaDiff = logDeltaDiff;
    }

    public boolean isDisableDelta() {
        return disableDelta;
    }

    public void setDisableDelta(boolean disableDelta) {
        this.disableDelta = disableDelta;
    }

    public String getFetchRemoteRegionsRegistry() {
        return fetchRemoteRegionsRegistry;
    }

    public void setFetchRemoteRegionsRegistry(String fetchRemoteRegionsRegistry) {
        this.fetchRemoteRegionsRegistry = fetchRemoteRegionsRegistry;
    }

    public Map<String, String> getAvailabilityZones() {
        return availabilityZones;
    }

    public void setAvailabilityZones(Map<String, String> availabilityZones) {
        this.availabilityZones = availabilityZones;
    }

    public boolean isFilterOnlyUpInstances() {
        return filterOnlyUpInstances;
    }

    public void setFilterOnlyUpInstances(boolean filterOnlyUpInstances) {
        this.filterOnlyUpInstances = filterOnlyUpInstances;
    }

    public boolean isFetchRegistry() {
        return fetchRegistry;
    }

    public void setFetchRegistry(boolean fetchRegistry) {
        this.fetchRegistry = fetchRegistry;
    }

    @Override
    public String getDollarReplacement() {
        return dollarReplacement;
    }

    public void setDollarReplacement(String dollarReplacement) {
        this.dollarReplacement = dollarReplacement;
    }

    @Override
    public String getEscapeCharReplacement() {
        return escapeCharReplacement;
    }

    public void setEscapeCharReplacement(String escapeCharReplacement) {
        this.escapeCharReplacement = escapeCharReplacement;
    }

    public boolean isAllowRedirects() {
        return allowRedirects;
    }

    public void setAllowRedirects(boolean allowRedirects) {
        this.allowRedirects = allowRedirects;
    }

    public boolean isOnDemandUpdateStatusChange() {
        return onDemandUpdateStatusChange;
    }

    public void setOnDemandUpdateStatusChange(boolean onDemandUpdateStatusChange) {
        this.onDemandUpdateStatusChange = onDemandUpdateStatusChange;
    }

    @Override
    public String getEncoderName() {
        return encoderName;
    }

    public void setEncoderName(String encoderName) {
        this.encoderName = encoderName;
    }

    @Override
    public String getDecoderName() {
        return decoderName;
    }

    public void setDecoderName(String decoderName) {
        this.decoderName = decoderName;
    }

    @Override
    public String getClientDataAccept() {
        return clientDataAccept;
    }

    public void setClientDataAccept(String clientDataAccept) {
        this.clientDataAccept = clientDataAccept;
    }

    public boolean isShouldUnregisterOnShutdown() {
        return shouldUnregisterOnShutdown;
    }

    public void setShouldUnregisterOnShutdown(boolean shouldUnregisterOnShutdown) {
        this.shouldUnregisterOnShutdown = shouldUnregisterOnShutdown;
    }

    public boolean isShouldEnforceRegistrationAtInit() {
        return shouldEnforceRegistrationAtInit;
    }

    public void setShouldEnforceRegistrationAtInit(boolean shouldEnforceRegistrationAtInit) {
        this.shouldEnforceRegistrationAtInit = shouldEnforceRegistrationAtInit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EurekaClientConfigBean that = (EurekaClientConfigBean) o;
        return Objects.equals(propertyResolver, that.propertyResolver) &&
                enabled == that.enabled &&
                Objects.equals(transport, that.transport) &&
                registryFetchIntervalSeconds == that.registryFetchIntervalSeconds &&
                instanceInfoReplicationIntervalSeconds == that.instanceInfoReplicationIntervalSeconds &&
                initialInstanceInfoReplicationIntervalSeconds == that.initialInstanceInfoReplicationIntervalSeconds &&
                eurekaServiceUrlPollIntervalSeconds == that.eurekaServiceUrlPollIntervalSeconds &&
                eurekaServerReadTimeoutSeconds == that.eurekaServerReadTimeoutSeconds &&
                eurekaServerConnectTimeoutSeconds == that.eurekaServerConnectTimeoutSeconds &&
                eurekaServerTotalConnections == that.eurekaServerTotalConnections &&
                eurekaServerTotalConnectionsPerHost == that.eurekaServerTotalConnectionsPerHost &&
                eurekaConnectionIdleTimeoutSeconds == that.eurekaConnectionIdleTimeoutSeconds &&
                heartbeatExecutorThreadPoolSize == that.heartbeatExecutorThreadPoolSize &&
                heartbeatExecutorExponentialBackOffBound == that.heartbeatExecutorExponentialBackOffBound &&
                cacheRefreshExecutorThreadPoolSize == that.cacheRefreshExecutorThreadPoolSize &&
                cacheRefreshExecutorExponentialBackOffBound == that.cacheRefreshExecutorExponentialBackOffBound &&
                gZipContent == that.gZipContent &&
                useDnsForFetchingServiceUrls == that.useDnsForFetchingServiceUrls &&
                registerWithEureka == that.registerWithEureka &&
                preferSameZoneEureka == that.preferSameZoneEureka &&
                logDeltaDiff == that.logDeltaDiff &&
                disableDelta == that.disableDelta &&
                filterOnlyUpInstances == that.filterOnlyUpInstances &&
                fetchRegistry == that.fetchRegistry &&
                allowRedirects == that.allowRedirects &&
                onDemandUpdateStatusChange == that.onDemandUpdateStatusChange &&
                shouldUnregisterOnShutdown == that.shouldUnregisterOnShutdown &&
                shouldEnforceRegistrationAtInit == that.shouldEnforceRegistrationAtInit &&
                Objects.equals(proxyPort, that.proxyPort) &&
                Objects.equals(proxyHost, that.proxyHost) &&
                Objects.equals(proxyUserName, that.proxyUserName) &&
                Objects.equals(proxyPassword, that.proxyPassword) &&
                Objects.equals(backupRegistryImpl, that.backupRegistryImpl) &&
                Objects.equals(eurekaServerURLContext, that.eurekaServerURLContext) &&
                Objects.equals(eurekaServerPort, that.eurekaServerPort) &&
                Objects.equals(eurekaServerDNSName, that.eurekaServerDNSName) &&
                Objects.equals(region, that.region) &&
                Objects.equals(registryRefreshSingleVipAddress, that.registryRefreshSingleVipAddress) &&
                Objects.equals(serviceUrl, that.serviceUrl) &&
                Objects.equals(fetchRemoteRegionsRegistry, that.fetchRemoteRegionsRegistry) &&
                Objects.equals(availabilityZones, that.availabilityZones) &&
                Objects.equals(dollarReplacement, that.dollarReplacement) &&
                Objects.equals(escapeCharReplacement, that.escapeCharReplacement) &&
                Objects.equals(encoderName, that.encoderName) &&
                Objects.equals(decoderName, that.decoderName) &&
                Objects.equals(clientDataAccept, that.clientDataAccept);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyResolver, enabled, transport,
                registryFetchIntervalSeconds, instanceInfoReplicationIntervalSeconds,
                initialInstanceInfoReplicationIntervalSeconds,
                eurekaServiceUrlPollIntervalSeconds, proxyPort, proxyHost, proxyUserName,
                proxyPassword, eurekaServerReadTimeoutSeconds,
                eurekaServerConnectTimeoutSeconds, backupRegistryImpl,
                eurekaServerTotalConnections, eurekaServerTotalConnectionsPerHost,
                eurekaServerURLContext, eurekaServerPort, eurekaServerDNSName, region,
                eurekaConnectionIdleTimeoutSeconds, registryRefreshSingleVipAddress,
                heartbeatExecutorThreadPoolSize, heartbeatExecutorExponentialBackOffBound,
                cacheRefreshExecutorThreadPoolSize,
                cacheRefreshExecutorExponentialBackOffBound, serviceUrl, gZipContent,
                useDnsForFetchingServiceUrls, registerWithEureka, preferSameZoneEureka,
                logDeltaDiff, disableDelta, fetchRemoteRegionsRegistry, availabilityZones,
                filterOnlyUpInstances, fetchRegistry, dollarReplacement,
                escapeCharReplacement, allowRedirects, onDemandUpdateStatusChange,
                encoderName, decoderName, clientDataAccept, shouldUnregisterOnShutdown,
                shouldEnforceRegistrationAtInit);
    }

    @Override
    public String toString() {
        return new StringBuilder("EurekaClientConfigBean{")
                .append("propertyResolver=").append(propertyResolver).append(", ")
                .append("enabled=").append(enabled).append(", ")
                .append("transport=").append(transport).append(", ")
                .append("registryFetchIntervalSeconds=").append(registryFetchIntervalSeconds).append(", ")
                .append("instanceInfoReplicationIntervalSeconds=").append(instanceInfoReplicationIntervalSeconds).append(", ")
                .append("initialInstanceInfoReplicationIntervalSeconds=").append(initialInstanceInfoReplicationIntervalSeconds).append(", ")
                .append("eurekaServiceUrlPollIntervalSeconds=").append(eurekaServiceUrlPollIntervalSeconds).append(", ")
                .append("proxyPort='").append(proxyPort).append("', ")
                .append("proxyHost='").append(proxyHost).append("', ")
                .append("proxyUserName='").append(proxyUserName).append("', ")
                .append("proxyPassword='").append(proxyPassword).append("', ")
                .append("eurekaServerReadTimeoutSeconds=").append(eurekaServerReadTimeoutSeconds).append(", ")
                .append("eurekaServerConnectTimeoutSeconds=").append(eurekaServerConnectTimeoutSeconds).append(", ")
                .append("backupRegistryImpl='").append(backupRegistryImpl).append("', ")
                .append("eurekaServerTotalConnections=").append(eurekaServerTotalConnections).append(", ")
                .append("eurekaServerTotalConnectionsPerHost=").append(eurekaServerTotalConnectionsPerHost).append(", ")
                .append("eurekaServerURLContext='").append(eurekaServerURLContext).append("', ")
                .append("eurekaServerPort='").append(eurekaServerPort).append("', ")
                .append("eurekaServerDNSName='").append(eurekaServerDNSName).append("', ")
                .append("region='").append(region).append("', ")
                .append("eurekaConnectionIdleTimeoutSeconds=").append(eurekaConnectionIdleTimeoutSeconds).append(", ")
                .append("registryRefreshSingleVipAddress='").append(registryRefreshSingleVipAddress).append("', ")
                .append("heartbeatExecutorThreadPoolSize=").append(heartbeatExecutorThreadPoolSize).append(", ")
                .append("heartbeatExecutorExponentialBackOffBound=").append(heartbeatExecutorExponentialBackOffBound).append(", ")
                .append("cacheRefreshExecutorThreadPoolSize=").append(cacheRefreshExecutorThreadPoolSize).append(", ")
                .append("cacheRefreshExecutorExponentialBackOffBound=").append(cacheRefreshExecutorExponentialBackOffBound).append(", ")
                .append("serviceUrl=").append(serviceUrl).append(", ")
                .append("gZipContent=").append(gZipContent).append(", ")
                .append("useDnsForFetchingServiceUrls=").append(useDnsForFetchingServiceUrls).append(", ")
                .append("registerWithEureka=").append(registerWithEureka).append(", ")
                .append("preferSameZoneEureka=").append(preferSameZoneEureka).append(", ")
                .append("logDeltaDiff=").append(logDeltaDiff).append(", ")
                .append("disableDelta=").append(disableDelta).append(", ")
                .append("fetchRemoteRegionsRegistry='").append(fetchRemoteRegionsRegistry).append("', ")
                .append("availabilityZones=").append(availabilityZones).append(", ")
                .append("filterOnlyUpInstances=").append(filterOnlyUpInstances).append(", ")
                .append("fetchRegistry=").append(fetchRegistry).append(", ")
                .append("dollarReplacement='").append(dollarReplacement).append("', ")
                .append("escapeCharReplacement='").append(escapeCharReplacement).append("', ")
                .append("allowRedirects=").append(allowRedirects).append(", ")
                .append("onDemandUpdateStatusChange=").append(onDemandUpdateStatusChange).append(", ")
                .append("encoderName='").append(encoderName).append("', ")
                .append("decoderName='").append(decoderName).append("', ")
                .append("clientDataAccept='").append(clientDataAccept).append("'").append("}")
                .append("=shouldUnregisterOnShutdown'").append(shouldUnregisterOnShutdown).append("'").append("}")
                .append("shouldEnforceRegistrationAtInit='").append(shouldEnforceRegistrationAtInit).append("'").append("}")
                .toString();
    }

}
