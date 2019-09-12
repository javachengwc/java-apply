package com.pseudocode.cloud.eurekaclient;
;
import java.util.HashMap;
import java.util.Map;

import com.pseudocode.cloud.commons.util.InetUtils;
import com.pseudocode.cloud.commons.util.InetUtils.HostInfo;
import com.pseudocode.netflix.eureka.client.appinfo.DataCenterInfo;
import com.pseudocode.netflix.eureka.client.appinfo.InstanceInfo.InstanceStatus;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

@ConfigurationProperties("eureka.instance")
public class EurekaInstanceConfigBean implements  EnvironmentAware {

    private static final String UNKNOWN = "unknown";

    private HostInfo hostInfo;

    private InetUtils inetUtils;

    private String actuatorPrefix = "/actuator";

    //获得在eureka服务上注册的应用程序的名字，默认为unknow
    private String appname = UNKNOWN;

    //获得在eureka服务上注册的应用程序组的名字，默认为unknow
    private String appGroupName;

    //实例注册到eureka服务器时，是否开启通讯，默认为false
    private boolean instanceEnabledOnit;

    //获取该实例应该接收通信的非安全端口。默认为80
    private int nonSecurePort = 80;

    private int securePort = 443;

    private boolean nonSecurePortEnabled = true;

    private boolean securePortEnabled;

    //eureka客户需要多长时间发送心跳给eureka服务器,默认为30 秒
    private int leaseRenewalIntervalInSeconds = 30;

    //Eureka服务器在接收到实例的最后一次发出的心跳后，需要等待多久才可以将此实例删除，默认为90秒
    private int leaseExpirationDurationInSeconds = 90;

    private String virtualHostName = UNKNOWN;

    //实例注册到eureka服务端的唯一的实例ID,其组成为${spring.application.name}:${spring.application.instance_id:${random.value}}
    private String instanceId;

    private String secureVirtualHostName = UNKNOWN;

    //与此实例相关联 AWS自动缩放组名称。此项配置是在AWS环境专门使用的实例启动，它已被用于流量停用后自动把一个实例退出服务。
    private String aSGName;

    private Map<String, String> metadataMap = new HashMap<>();

    //private DataCenterInfo dataCenterInfo = new MyDataCenterInfo(DataCenterInfo.Name.MyOwn);
    private DataCenterInfo dataCenterInfo =null;

    private String ipAddress;

    private String statusPageUrlPath = actuatorPrefix + "/info";

    private String statusPageUrl;

    private String homePageUrlPath = "/";

    private String homePageUrl;

    private String healthCheckUrlPath = actuatorPrefix + "/health";

    private String healthCheckUrl;

    private String secureHealthCheckUrl;

    private String namespace = "eureka";

    private String hostname;

    //eureka.instance.prefer-ip-address = true时，注册到Eureka Server上的是IP
    private boolean preferIpAddress = false;

    private InstanceStatus initialStatus = InstanceStatus.UP;

    private String[] defaultAddressResolutionOrder = new String[0];
    private Environment environment;

    public String getHostname() {
        return getHostName(false);
    }

    @SuppressWarnings("unused")
    private EurekaInstanceConfigBean() {
    }

    public EurekaInstanceConfigBean(InetUtils inetUtils) {
        this.inetUtils = inetUtils;
        this.hostInfo = this.inetUtils.findFirstNonLoopbackHostInfo();
        this.ipAddress = this.hostInfo.getIpAddress();
        this.hostname = this.hostInfo.getHostname();
    }

    public String getInstanceId() {
        if (this.instanceId == null && this.metadataMap != null) {
            return this.metadataMap.get("instanceId");
        }
        return this.instanceId;
    }

    public boolean getSecurePortEnabled() {
        return this.securePortEnabled;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
        this.hostInfo.override = true;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        this.hostInfo.override = true;
    }

    public String getHostName(boolean refresh) {
        if (refresh && !this.hostInfo.override) {
            this.ipAddress = this.hostInfo.getIpAddress();
            this.hostname = this.hostInfo.getHostname();
        }
        return this.preferIpAddress ? this.ipAddress : this.hostname;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        // set some defaults from the environment, but allow the defaults to use relaxed binding
        String springAppName = this.environment.getProperty("spring.application.name", "");
        if(StringUtils.hasText(springAppName)) {
            setAppname(springAppName);
            setVirtualHostName(springAppName);
            setSecureVirtualHostName(springAppName);
        }
    }

    private HostInfo getHostInfo() {
        return hostInfo;
    }

    private void setHostInfo(HostInfo hostInfo) {
        this.hostInfo = hostInfo;
    }

    private InetUtils getInetUtils() {
        return inetUtils;
    }

    private void setInetUtils(InetUtils inetUtils) {
        this.inetUtils = inetUtils;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getAppGroupName() {
        return appGroupName;
    }

    public void setAppGroupName(String appGroupName) {
        this.appGroupName = appGroupName;
    }

    public boolean isInstanceEnabledOnit() {
        return instanceEnabledOnit;
    }

    public void setInstanceEnabledOnit(boolean instanceEnabledOnit) {
        this.instanceEnabledOnit = instanceEnabledOnit;
    }

    public int getNonSecurePort() {
        return nonSecurePort;
    }

    public void setNonSecurePort(int nonSecurePort) {
        this.nonSecurePort = nonSecurePort;
    }

    public int getSecurePort() {
        return securePort;
    }

    public void setSecurePort(int securePort) {
        this.securePort = securePort;
    }

    public boolean isNonSecurePortEnabled() {
        return nonSecurePortEnabled;
    }

    public void setNonSecurePortEnabled(boolean nonSecurePortEnabled) {
        this.nonSecurePortEnabled = nonSecurePortEnabled;
    }

    public boolean isSecurePortEnabled() {
        return securePortEnabled;
    }

    public void setSecurePortEnabled(boolean securePortEnabled) {
        this.securePortEnabled = securePortEnabled;
    }

    public int getLeaseRenewalIntervalInSeconds() {
        return leaseRenewalIntervalInSeconds;
    }

    public void setLeaseRenewalIntervalInSeconds(int leaseRenewalIntervalInSeconds) {
        this.leaseRenewalIntervalInSeconds = leaseRenewalIntervalInSeconds;
    }

    public int getLeaseExpirationDurationInSeconds() {
        return leaseExpirationDurationInSeconds;
    }

    public void setLeaseExpirationDurationInSeconds(
            int leaseExpirationDurationInSeconds) {
        this.leaseExpirationDurationInSeconds = leaseExpirationDurationInSeconds;
    }

    public String getVirtualHostName() {
        return virtualHostName;
    }

    public void setVirtualHostName(String virtualHostName) {
        this.virtualHostName = virtualHostName;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getSecureVirtualHostName() {
        return secureVirtualHostName;
    }

    public void setSecureVirtualHostName(String secureVirtualHostName) {
        this.secureVirtualHostName = secureVirtualHostName;
    }

    public String getASGName() {
        return aSGName;
    }

    public void setASGName(String aSGName) {
        this.aSGName = aSGName;
    }

    public Map<String, String> getMetadataMap() {
        return metadataMap;
    }

    public void setMetadataMap(Map<String, String> metadataMap) {
        this.metadataMap = metadataMap;
    }

    public DataCenterInfo getDataCenterInfo() {
        return dataCenterInfo;
    }

    public void setDataCenterInfo(DataCenterInfo dataCenterInfo) {
        this.dataCenterInfo = dataCenterInfo;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getStatusPageUrlPath() {
        return statusPageUrlPath;
    }

    public void setStatusPageUrlPath(String statusPageUrlPath) {
        this.statusPageUrlPath = statusPageUrlPath;
    }

    public String getStatusPageUrl() {
        return statusPageUrl;
    }

    public void setStatusPageUrl(String statusPageUrl) {
        this.statusPageUrl = statusPageUrl;
    }

    public String getHomePageUrlPath() {
        return homePageUrlPath;
    }

    public void setHomePageUrlPath(String homePageUrlPath) {
        this.homePageUrlPath = homePageUrlPath;
    }

    public String getHomePageUrl() {
        return homePageUrl;
    }

    public void setHomePageUrl(String homePageUrl) {
        this.homePageUrl = homePageUrl;
    }

    public String getHealthCheckUrlPath() {
        return healthCheckUrlPath;
    }

    public void setHealthCheckUrlPath(String healthCheckUrlPath) {
        this.healthCheckUrlPath = healthCheckUrlPath;
    }

    public String getHealthCheckUrl() {
        return healthCheckUrl;
    }

    public void setHealthCheckUrl(String healthCheckUrl) {
        this.healthCheckUrl = healthCheckUrl;
    }

    public String getSecureHealthCheckUrl() {
        return secureHealthCheckUrl;
    }

    public void setSecureHealthCheckUrl(String secureHealthCheckUrl) {
        this.secureHealthCheckUrl = secureHealthCheckUrl;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public boolean isPreferIpAddress() {
        return preferIpAddress;
    }

    public void setPreferIpAddress(boolean preferIpAddress) {
        this.preferIpAddress = preferIpAddress;
    }

    public InstanceStatus getInitialStatus() {
        return initialStatus;
    }

    public void setInitialStatus(InstanceStatus initialStatus) {
        this.initialStatus = initialStatus;
    }

    public String[] getDefaultAddressResolutionOrder() {
        return defaultAddressResolutionOrder;
    }

    public void setDefaultAddressResolutionOrder(String[] defaultAddressResolutionOrder) {
        this.defaultAddressResolutionOrder = defaultAddressResolutionOrder;
    }

    public Environment getEnvironment() {
        return environment;
    }

}
