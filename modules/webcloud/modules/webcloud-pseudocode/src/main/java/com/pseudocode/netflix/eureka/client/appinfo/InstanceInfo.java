package com.pseudocode.netflix.eureka.client.appinfo;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.pseudocode.netflix.eureka.client.appinfo.providers.VipAddressResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonRootName("instance")
public class InstanceInfo {

    private static final String VERSION_UNKNOWN = "unknown";

    public static class PortWrapper {
        private final boolean enabled;
        private final int port;

        @JsonCreator
        public PortWrapper(@JsonProperty("@enabled") boolean enabled, @JsonProperty("$") int port) {
            this.enabled = enabled;
            this.port = port;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public int getPort() {
            return port;
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(InstanceInfo.class);

    public static final int DEFAULT_PORT = 7001;
    public static final int DEFAULT_SECURE_PORT = 7002;
    public static final int DEFAULT_COUNTRY_ID = 1; // US

    private volatile String instanceId;

    private volatile String appName;

    private volatile String appGroupName;

    private volatile String ipAddr;

    private static final String SID_DEFAULT = "na";
    @Deprecated
    private volatile String sid = SID_DEFAULT;

    private volatile int port = DEFAULT_PORT;
    private volatile int securePort = DEFAULT_SECURE_PORT;

    private volatile String homePageUrl;

    private volatile String statusPageUrl;

    private volatile String healthCheckUrl;

    private volatile String secureHealthCheckUrl;
    private volatile String vipAddress;
    private volatile String secureVipAddress;
    private String statusPageRelativeUrl;
    private String statusPageExplicitUrl;
    private String healthCheckRelativeUrl;
    private String healthCheckSecureExplicitUrl;
    private String vipAddressUnresolved;
    private String secureVipAddressUnresolved;
    private String healthCheckExplicitUrl;
    @Deprecated
    private volatile int countryId = DEFAULT_COUNTRY_ID; // Defaults to US
    private volatile boolean isSecurePortEnabled = false;
    private volatile boolean isUnsecurePortEnabled = true;
    private volatile DataCenterInfo dataCenterInfo;
    private volatile String hostName;

    //状态
    private volatile InstanceStatus status = InstanceStatus.UP;

    //覆盖状态
    private volatile InstanceStatus overriddenStatus = InstanceStatus.UNKNOWN;

    private volatile boolean isInstanceInfoDirty = false;
    private volatile LeaseInfo leaseInfo;
    private volatile Boolean isCoordinatingDiscoveryServer = Boolean.FALSE;
    private volatile Map<String, String> metadata;
    private volatile Long lastUpdatedTimestamp;
    private volatile Long lastDirtyTimestamp;
    private volatile ActionType actionType;
    private volatile String asgName;
    private String version = VERSION_UNKNOWN;

    private InstanceInfo() {
        this.metadata = new ConcurrentHashMap<String, String>();
        this.lastUpdatedTimestamp = System.currentTimeMillis();
        this.lastDirtyTimestamp = lastUpdatedTimestamp;
    }

    @JsonCreator
    public InstanceInfo(
            @JsonProperty("instanceId") String instanceId,
            @JsonProperty("app") String appName,
            @JsonProperty("appGroupName") String appGroupName,
            @JsonProperty("ipAddr") String ipAddr,
            @JsonProperty("sid") String sid,
            @JsonProperty("port") PortWrapper port,
            @JsonProperty("securePort") PortWrapper securePort,
            @JsonProperty("homePageUrl") String homePageUrl,
            @JsonProperty("statusPageUrl") String statusPageUrl,
            @JsonProperty("healthCheckUrl") String healthCheckUrl,
            @JsonProperty("secureHealthCheckUrl") String secureHealthCheckUrl,
            @JsonProperty("vipAddress") String vipAddress,
            @JsonProperty("secureVipAddress") String secureVipAddress,
            @JsonProperty("countryId") int countryId,
            @JsonProperty("dataCenterInfo") DataCenterInfo dataCenterInfo,
            @JsonProperty("hostName") String hostName,
            @JsonProperty("status") InstanceStatus status,
            @JsonProperty("overriddenstatus") InstanceStatus overriddenStatus,
            @JsonProperty("overriddenStatus") InstanceStatus overriddenStatusAlt,
            @JsonProperty("leaseInfo") LeaseInfo leaseInfo,
            @JsonProperty("isCoordinatingDiscoveryServer") Boolean isCoordinatingDiscoveryServer,
            @JsonProperty("metadata") HashMap<String, String> metadata,
            @JsonProperty("lastUpdatedTimestamp") Long lastUpdatedTimestamp,
            @JsonProperty("lastDirtyTimestamp") Long lastDirtyTimestamp,
            @JsonProperty("actionType") ActionType actionType,
            @JsonProperty("asgName") String asgName) {
        this.instanceId = instanceId;
        this.sid = sid;
//        this.appName = StringCache.intern(appName);
//        this.appGroupName = StringCache.intern(appGroupName);
        this.ipAddr = ipAddr;
        this.port = port == null ? 0 : port.getPort();
        this.isUnsecurePortEnabled = port != null && port.isEnabled();
        this.securePort = securePort == null ? 0 : securePort.getPort();
        this.isSecurePortEnabled = securePort != null && securePort.isEnabled();
        this.homePageUrl = homePageUrl;
        this.statusPageUrl = statusPageUrl;
        this.healthCheckUrl = healthCheckUrl;
        this.secureHealthCheckUrl = secureHealthCheckUrl;
//        this.vipAddress = StringCache.intern(vipAddress);
//        this.secureVipAddress = StringCache.intern(secureVipAddress);
        this.countryId = countryId;
        this.dataCenterInfo = dataCenterInfo;
        this.hostName = hostName;
        this.status = status;
        this.overriddenStatus = overriddenStatus == null ? overriddenStatusAlt : overriddenStatus;
        this.leaseInfo = leaseInfo;
        this.isCoordinatingDiscoveryServer = isCoordinatingDiscoveryServer;
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
        this.lastDirtyTimestamp = lastDirtyTimestamp;
        this.actionType = actionType;
//        this.asgName = StringCache.intern(asgName);

        if (metadata == null) {
            this.metadata = Collections.emptyMap();
        } else if (metadata.size() == 1) {
            this.metadata = removeMetadataMapLegacyValues(metadata);
        } else {
            this.metadata = metadata;
        }

        if (sid == null) {
            this.sid = SID_DEFAULT;
        }
    }

    private Map<String, String> removeMetadataMapLegacyValues(Map<String, String> metadata) {
        return metadata;
    }

    public InstanceInfo(InstanceInfo ii) {
        this.instanceId = ii.instanceId;
        this.appName = ii.appName;
        this.appGroupName = ii.appGroupName;
        this.ipAddr = ii.ipAddr;
        this.sid = ii.sid;

        this.port = ii.port;
        this.securePort = ii.securePort;

        this.homePageUrl = ii.homePageUrl;
        this.statusPageUrl = ii.statusPageUrl;
        this.healthCheckUrl = ii.healthCheckUrl;
        this.secureHealthCheckUrl = ii.secureHealthCheckUrl;

        this.vipAddress = ii.vipAddress;
        this.secureVipAddress = ii.secureVipAddress;
        this.statusPageRelativeUrl = ii.statusPageRelativeUrl;
        this.statusPageExplicitUrl = ii.statusPageExplicitUrl;

        this.healthCheckRelativeUrl = ii.healthCheckRelativeUrl;
        this.healthCheckSecureExplicitUrl = ii.healthCheckSecureExplicitUrl;

        this.vipAddressUnresolved = ii.vipAddressUnresolved;
        this.secureVipAddressUnresolved = ii.secureVipAddressUnresolved;

        this.healthCheckExplicitUrl = ii.healthCheckExplicitUrl;

        this.countryId = ii.countryId;
        this.isSecurePortEnabled = ii.isSecurePortEnabled;
        this.isUnsecurePortEnabled = ii.isUnsecurePortEnabled;

        this.dataCenterInfo = ii.dataCenterInfo;

        this.hostName = ii.hostName;

        this.status = ii.status;
        this.overriddenStatus = ii.overriddenStatus;

        this.isInstanceInfoDirty = ii.isInstanceInfoDirty;

        this.leaseInfo = ii.leaseInfo;

        this.isCoordinatingDiscoveryServer = ii.isCoordinatingDiscoveryServer;

        this.metadata = ii.metadata;

        this.lastUpdatedTimestamp = ii.lastUpdatedTimestamp;
        this.lastDirtyTimestamp = ii.lastDirtyTimestamp;

        this.actionType = ii.actionType;

        this.asgName = ii.asgName;

        this.version = ii.version;
    }


    public enum InstanceStatus {
        UP, // Ready to receive traffic
        DOWN, // Do not send traffic- healthcheck callback failed
        STARTING, // Just about starting- initializations to be done - do not
        // send traffic
        OUT_OF_SERVICE, // Intentionally shutdown for traffic
        UNKNOWN;

        public static InstanceStatus toEnum(String s) {
            if (s != null) {
                try {
                    return InstanceStatus.valueOf(s.toUpperCase());
                } catch (IllegalArgumentException e) {
                    // ignore and fall through to unknown
                    logger.debug("illegal argument supplied to InstanceStatus.valueOf: {}, defaulting to {}", s, UNKNOWN);
                }
            }
            return UNKNOWN;
        }
    }

    @Override
    public int hashCode() {
        String id = getId();
        return (id == null) ? 31 : (id.hashCode() + 31);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        InstanceInfo other = (InstanceInfo) obj;
        String id = getId();
        if (id == null) {
            if (other.getId() != null) {
                return false;
            }
        } else if (!id.equals(other.getId())) {
            return false;
        }
        return true;
    }

    public enum PortType {
        SECURE, UNSECURE
    }

    public static final class Builder {
        private static final String COLON = ":";
        private static final String HTTPS_PROTOCOL = "https://";
        private static final String HTTP_PROTOCOL = "http://";
        private final Function<String,String> intern;

        private static final class LazyHolder {
//            private static final VipAddressResolver DEFAULT_VIP_ADDRESS_RESOLVER = new Archaius1VipAddressResolver();
            private static final VipAddressResolver DEFAULT_VIP_ADDRESS_RESOLVER = null;
        }

        private InstanceInfo result;

        private final VipAddressResolver vipAddressResolver;

        private String namespace;

        private Builder(InstanceInfo result, VipAddressResolver vipAddressResolver, Function<String,String> intern) {
            this.vipAddressResolver = vipAddressResolver;
            this.result = result;
            //this.intern = intern != null ? intern : StringCache::intern;
            this.intern = intern;
        }

        public Builder(InstanceInfo instanceInfo) {
            this(instanceInfo, LazyHolder.DEFAULT_VIP_ADDRESS_RESOLVER, null);
        }

        public static Builder newBuilder() {
            return new Builder(new InstanceInfo(), LazyHolder.DEFAULT_VIP_ADDRESS_RESOLVER, null);
        }

        public static Builder newBuilder(Function<String,String> intern) {
            return new Builder(new InstanceInfo(), LazyHolder.DEFAULT_VIP_ADDRESS_RESOLVER, intern);
        }

        public static Builder newBuilder(VipAddressResolver vipAddressResolver) {
            return new Builder(new InstanceInfo(), vipAddressResolver, null);
        }

        public Builder setInstanceId(String instanceId) {
            result.instanceId = instanceId;
            return this;
        }

        public Builder setAppName(String appName) {
            result.appName = intern.apply(appName.toUpperCase(Locale.ROOT));
            return this;
        }

        public Builder setAppNameForDeser(String appName) {
            result.appName = appName;
            return this;
        }


        public Builder setAppGroupName(String appGroupName) {
            if (appGroupName != null) {
                result.appGroupName = intern.apply(appGroupName.toUpperCase(Locale.ROOT));
            } else {
                result.appGroupName = null;
            }
            return this;
        }
        public Builder setAppGroupNameForDeser(String appGroupName) {
            result.appGroupName = appGroupName;
            return this;
        }

        public Builder setHostName(String hostName) {
            if (hostName == null || hostName.isEmpty()) {
                logger.warn("Passed in hostname is blank, not setting it");
                return this;
            }

            String existingHostName = result.hostName;
            result.hostName = hostName;
            if ((existingHostName != null)
                    && !(hostName.equals(existingHostName))) {
                refreshStatusPageUrl().refreshHealthCheckUrl()
                        .refreshVIPAddress().refreshSecureVIPAddress();
            }
            return this;
        }

        public Builder setStatus(InstanceStatus status) {
            result.status = status;
            return this;
        }

        public Builder setOverriddenStatus(InstanceStatus status) {
            result.overriddenStatus = status;
            return this;
        }

        public Builder setIPAddr(String ip) {
            result.ipAddr = ip;
            return this;
        }

        @Deprecated
        public Builder setSID(String sid) {
            result.sid = sid;
            return this;
        }

        public Builder setPort(int port) {
            result.port = port;
            return this;
        }

        public Builder setSecurePort(int port) {
            result.securePort = port;
            return this;
        }

        public Builder enablePort(PortType type, boolean isEnabled) {
            if (type == PortType.SECURE) {
                result.isSecurePortEnabled = isEnabled;
            } else {
                result.isUnsecurePortEnabled = isEnabled;
            }
            return this;
        }

        @Deprecated
        public Builder setCountryId(int id) {
            result.countryId = id;
            return this;
        }

        public Builder setHomePageUrl(String relativeUrl, String explicitUrl) {
            String hostNameInterpolationExpression = "${" + namespace + "hostname}";
            if (explicitUrl != null) {
                result.homePageUrl = explicitUrl.replace(
                        hostNameInterpolationExpression, result.hostName);
            } else if (relativeUrl != null) {
                result.homePageUrl = HTTP_PROTOCOL + result.hostName + COLON
                        + result.port + relativeUrl;
            }
            return this;
        }

        public Builder setHomePageUrlForDeser(String homePageUrl) {
            result.homePageUrl = homePageUrl;
            return this;
        }

        public Builder setStatusPageUrl(String relativeUrl, String explicitUrl) {
            String hostNameInterpolationExpression = "${" + namespace + "hostname}";
            result.statusPageRelativeUrl = relativeUrl;
            result.statusPageExplicitUrl = explicitUrl;
            if (explicitUrl != null) {
                result.statusPageUrl = explicitUrl.replace(
                        hostNameInterpolationExpression, result.hostName);
            } else if (relativeUrl != null) {
                result.statusPageUrl = HTTP_PROTOCOL + result.hostName + COLON
                        + result.port + relativeUrl;
            }
            return this;
        }

        public Builder setStatusPageUrlForDeser(String statusPageUrl) {
            result.statusPageUrl = statusPageUrl;
            return this;
        }

        public Builder setHealthCheckUrls(String relativeUrl,
                                          String explicitUrl, String secureExplicitUrl) {
            String hostNameInterpolationExpression = "${" + namespace + "hostname}";
            result.healthCheckRelativeUrl = relativeUrl;
            result.healthCheckExplicitUrl = explicitUrl;
            result.healthCheckSecureExplicitUrl = secureExplicitUrl;
            if (explicitUrl != null) {
                result.healthCheckUrl = explicitUrl.replace(
                        hostNameInterpolationExpression, result.hostName);
            } else if (result.isUnsecurePortEnabled) {
                result.healthCheckUrl = HTTP_PROTOCOL + result.hostName + COLON
                        + result.port + relativeUrl;
            }

            if (secureExplicitUrl != null) {
                result.secureHealthCheckUrl = secureExplicitUrl.replace(
                        hostNameInterpolationExpression, result.hostName);
            } else if (result.isSecurePortEnabled) {
                result.secureHealthCheckUrl = HTTPS_PROTOCOL + result.hostName
                        + COLON + result.securePort + relativeUrl;
            }
            return this;
        }

        public Builder setHealthCheckUrlsForDeser(String healthCheckUrl, String secureHealthCheckUrl) {
            if (healthCheckUrl != null) {
                result.healthCheckUrl = healthCheckUrl;
            }
            if (secureHealthCheckUrl != null) {
                result.secureHealthCheckUrl = secureHealthCheckUrl;
            }
            return this;
        }

        public Builder setVIPAddress(final String vipAddress) {
            result.vipAddressUnresolved = intern.apply(vipAddress);
            //result.vipAddress = intern.apply(vipAddressResolver.resolveDeploymentContextBasedVipAddresses(vipAddress));
            return this;
        }

        public Builder setVIPAddressDeser(String vipAddress) {
            result.vipAddress = intern.apply(vipAddress);
            return this;
        }

        public Builder setSecureVIPAddress(final String secureVIPAddress) {
            result.secureVipAddressUnresolved = intern.apply(secureVIPAddress);
            //result.secureVipAddress = intern.apply(vipAddressResolver.resolveDeploymentContextBasedVipAddresses(secureVIPAddress));
            return this;
        }

        public Builder setSecureVIPAddressDeser(String secureVIPAddress) {
            result.secureVipAddress = intern.apply(secureVIPAddress);
            return this;
        }

        public Builder setDataCenterInfo(DataCenterInfo datacenter) {
            result.dataCenterInfo = datacenter;
            return this;
        }

        public Builder setLeaseInfo(LeaseInfo info) {
            result.leaseInfo = info;
            return this;
        }

        public Builder add(String key, String val) {
            result.metadata.put(key, val);
            return this;
        }

        public Builder setMetadata(Map<String, String> mt) {
            result.metadata = mt;
            return this;
        }

        public InstanceInfo getRawInstance() {
            return result;
        }

        public InstanceInfo build() {
            if (!isInitialized()) {
                throw new IllegalStateException("name is required!");
            }
            return result;
        }

        public boolean isInitialized() {
            return (result.appName != null);
        }

        public Builder setASGName(String asgName) {
            result.asgName = intern.apply(asgName);
            return this;
        }

        private Builder refreshStatusPageUrl() {
            setStatusPageUrl(result.statusPageRelativeUrl,
                    result.statusPageExplicitUrl);
            return this;
        }

        private Builder refreshHealthCheckUrl() {
            setHealthCheckUrls(result.healthCheckRelativeUrl,
                    result.healthCheckExplicitUrl,
                    result.healthCheckSecureExplicitUrl);
            return this;
        }

        private Builder refreshSecureVIPAddress() {
            setSecureVIPAddress(result.secureVipAddressUnresolved);
            return this;
        }

        private Builder refreshVIPAddress() {
            setVIPAddress(result.vipAddressUnresolved);
            return this;
        }

        public Builder setIsCoordinatingDiscoveryServer(boolean isCoordinatingDiscoveryServer) {
            result.isCoordinatingDiscoveryServer = isCoordinatingDiscoveryServer;
            return this;
        }

        public Builder setLastUpdatedTimestamp(long lastUpdatedTimestamp) {
            result.lastUpdatedTimestamp = lastUpdatedTimestamp;
            return this;
        }

        public Builder setLastDirtyTimestamp(long lastDirtyTimestamp) {
            result.lastDirtyTimestamp = lastDirtyTimestamp;
            return this;
        }

        public Builder setActionType(ActionType actionType) {
            result.actionType = actionType;
            return this;
        }

        public Builder setNamespace(String namespace) {
            this.namespace = namespace.endsWith(".")
                    ? namespace
                    : namespace + ".";
            return this;
        }
    }

    public String getInstanceId() {
        return instanceId;
    }

    @JsonProperty("app")
    public String getAppName() {
        return appName;
    }

    public String getAppGroupName() {
        return appGroupName;
    }

    public String getHostName() {
        return hostName;
    }

    @Deprecated
    public void setSID(String sid) {
        this.sid = sid;
        setIsDirty();
    }

    @JsonProperty("sid")
    @Deprecated
    public String getSID() {
        return sid;
    }

    @JsonIgnore
    public String getId() {
        if (instanceId != null && !instanceId.isEmpty()) {
            return instanceId;
        }
//        else if (dataCenterInfo instanceof UniqueIdentifier) {
//            String uniqueId = ((UniqueIdentifier) dataCenterInfo).getId();
//            if (uniqueId != null && !uniqueId.isEmpty()) {
//                return uniqueId;
//            }
//        }
        return hostName;
    }

    @JsonProperty("ipAddr")
    public String getIPAddr() {
        return ipAddr;
    }

    @JsonIgnore
    public int getPort() {
        return port;
    }

    public InstanceStatus getStatus() {
        return status;
    }

    public InstanceStatus getOverriddenStatus() {
        return overriddenStatus;
    }

    public DataCenterInfo getDataCenterInfo() {
        return dataCenterInfo;
    }

    public LeaseInfo getLeaseInfo() {
        return leaseInfo;
    }

    public void setLeaseInfo(LeaseInfo info) {
        leaseInfo = info;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    @Deprecated
    public int getCountryId() {
        return countryId;
    }

    @JsonIgnore
    public int getSecurePort() {
        return securePort;
    }

    @JsonIgnore
    public boolean isPortEnabled(PortType type) {
        if (type == PortType.SECURE) {
            return isSecurePortEnabled;
        } else {
            return isUnsecurePortEnabled;
        }
    }

    public long getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    public void setLastUpdatedTimestamp() {
        this.lastUpdatedTimestamp = System.currentTimeMillis();
    }

    public String getHomePageUrl() {
        return homePageUrl;
    }

    public String getStatusPageUrl() {
        return statusPageUrl;
    }

    @JsonIgnore
    public Set<String> getHealthCheckUrls() {
        Set<String> healthCheckUrlSet = new LinkedHashSet<String>();
        if (this.isUnsecurePortEnabled && healthCheckUrl != null && !healthCheckUrl.isEmpty()) {
            healthCheckUrlSet.add(healthCheckUrl);
        }
        if (this.isSecurePortEnabled && secureHealthCheckUrl != null && !secureHealthCheckUrl.isEmpty()) {
            healthCheckUrlSet.add(secureHealthCheckUrl);
        }
        return healthCheckUrlSet;
    }

    public String getHealthCheckUrl() {
        return healthCheckUrl;
    }

    public String getSecureHealthCheckUrl() {
        return secureHealthCheckUrl;
    }

    @JsonProperty("vipAddress")
    public String getVIPAddress() {
        return vipAddress;
    }

    public String getSecureVipAddress() {
        return secureVipAddress;
    }

    public Long getLastDirtyTimestamp() {
        return lastDirtyTimestamp;
    }

    public void setLastDirtyTimestamp(Long lastDirtyTimestamp) {
        this.lastDirtyTimestamp = lastDirtyTimestamp;
    }

    public synchronized InstanceStatus setStatus(InstanceStatus status) {
        if (this.status != status) {
            InstanceStatus prev = this.status;
            this.status = status;
            setIsDirty();
            return prev;
        }
        return null;
    }

    public synchronized void setStatusWithoutDirty(InstanceStatus status) {
        if (this.status != status) {
            this.status = status;
        }
    }

    public synchronized void setOverriddenStatus(InstanceStatus status) {
        if (this.overriddenStatus != status) {
            this.overriddenStatus = status;
        }
    }

    @JsonIgnore
    public boolean isDirty() {
        return isInstanceInfoDirty;
    }

    public synchronized Long isDirtyWithTime() {
        if (isInstanceInfoDirty) {
            return lastDirtyTimestamp;
        } else {
            return null;
        }
    }

    @Deprecated
    public synchronized void setIsDirty(boolean isDirty) {
        if (isDirty) {
            setIsDirty();
        } else {
            isInstanceInfoDirty = false;
            // else don't update lastDirtyTimestamp as we are setting isDirty to false
        }
    }

    public synchronized void setIsDirty() {
        isInstanceInfoDirty = true;
        lastDirtyTimestamp = System.currentTimeMillis();
    }

    public synchronized long setIsDirtyWithTime() {
        setIsDirty();
        return lastDirtyTimestamp;
    }

    public synchronized void unsetIsDirty(long unsetDirtyTimestamp) {
        if (lastDirtyTimestamp <= unsetDirtyTimestamp) {
            isInstanceInfoDirty = false;
        } else {
        }
    }

    public void setIsCoordinatingDiscoveryServer() {
        String instanceId = getId();
//        if ((instanceId != null) && (instanceId.equals(ApplicationInfoManager.getInstance().getInfo().getId()))) {
        if ((instanceId != null)) {
            isCoordinatingDiscoveryServer = Boolean.TRUE;
        } else {
            isCoordinatingDiscoveryServer = Boolean.FALSE;
        }
    }

    @JsonProperty("isCoordinatingDiscoveryServer")
    public Boolean isCoordinatingDiscoveryServer() {
        return isCoordinatingDiscoveryServer;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    @JsonProperty("asgName")
    public String getASGName() {
        return this.asgName;
    }

    @Deprecated
    @JsonIgnore
    public String getVersion() {
        return version;
    }

    public enum ActionType {
        ADDED, // Added in the discovery server
        MODIFIED, // Changed in the discovery server
        DELETED
        // Deleted from the discovery server
    }

    synchronized void registerRuntimeMetadata(Map<String, String> runtimeMetadata) {
        metadata.putAll(runtimeMetadata);
        setIsDirty();
    }

    public static String getZone(String[] availZones, InstanceInfo myInfo) {
        String instanceZone = ((availZones == null || availZones.length == 0) ? "default" : availZones[0]);
        if (myInfo != null && myInfo.getDataCenterInfo().getName() == DataCenterInfo.Name.Amazon) {
//            String awsInstanceZone = ((AmazonInfo) myInfo.getDataCenterInfo()).get(AmazonInfo.MetaDataKey.availabilityZone);
            String awsInstanceZone= null;
            if (awsInstanceZone != null) {
                instanceZone = awsInstanceZone;
            }
        }
        return instanceZone;
    }
}

