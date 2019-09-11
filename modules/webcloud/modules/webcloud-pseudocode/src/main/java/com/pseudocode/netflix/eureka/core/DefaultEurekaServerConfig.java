package com.pseudocode.netflix.eureka.core;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import com.netflix.config.DynamicStringSetProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultEurekaServerConfig implements EurekaServerConfig {
    private static final String ARCHAIUS_DEPLOYMENT_ENVIRONMENT = "archaius.deployment.environment";
    private static final String TEST = "test";
    private static final String EUREKA_ENVIRONMENT = "eureka.environment";
    private static final Logger logger = LoggerFactory.getLogger(DefaultEurekaServerConfig.class);
    private static final DynamicPropertyFactory configInstance = com.netflix.config.DynamicPropertyFactory.getInstance();
    private static final DynamicStringProperty EUREKA_PROPS_FILE = DynamicPropertyFactory.getInstance().getStringProperty("eureka.server.props", "eureka-server");
    private static final int TIME_TO_WAIT_FOR_REPLICATION = 30000;

    private String namespace = "eureka.";

    // These counters are checked for each HTTP request. Instantiating them per request like for the other
    // properties would be too costly.
    private final DynamicStringSetProperty rateLimiterPrivilegedClients =
            new DynamicStringSetProperty(namespace + "rateLimiter.privilegedClients", Collections.<String>emptySet());
    private final DynamicBooleanProperty rateLimiterEnabled = configInstance.getBooleanProperty(namespace + "rateLimiter.enabled", false);
    private final DynamicBooleanProperty rateLimiterThrottleStandardClients = configInstance.getBooleanProperty(namespace + "rateLimiter.throttleStandardClients", false);
    private final DynamicIntProperty rateLimiterBurstSize = configInstance.getIntProperty(namespace + "rateLimiter.burstSize", 10);
    private final DynamicIntProperty rateLimiterRegistryFetchAverageRate = configInstance.getIntProperty(namespace + "rateLimiter.registryFetchAverageRate", 500);
    private final DynamicIntProperty rateLimiterFullFetchAverageRate = configInstance.getIntProperty(namespace + "rateLimiter.fullFetchAverageRate", 100);

    private final DynamicStringProperty listAutoScalingGroupsRoleName =
            configInstance.getStringProperty(namespace + "listAutoScalingGroupsRoleName", "ListAutoScalingGroups");

    public DefaultEurekaServerConfig() {
        init();
    }

    public DefaultEurekaServerConfig(String namespace) {
        this.namespace = namespace;
        init();
    }

    private void init() {
        String env = ConfigurationManager.getConfigInstance().getString(EUREKA_ENVIRONMENT, TEST);
        ConfigurationManager.getConfigInstance().setProperty(ARCHAIUS_DEPLOYMENT_ENVIRONMENT, env);
        String eurekaPropsFile = EUREKA_PROPS_FILE.get();
        try {
            // ConfigurationManager
            // .loadPropertiesFromResources(eurekaPropsFile);
            ConfigurationManager.loadCascadedPropertiesFromResources(eurekaPropsFile);
        } catch (IOException e) {
            logger.warn("Cannot find the properties specified : {}. This may be okay if there are other environment "
                            + "specific properties or the configuration is installed with a different mechanism.", eurekaPropsFile);
        }
    }

    @Override
    public String getAWSAccessId() {
        String aWSAccessId = configInstance.getStringProperty(namespace + "awsAccessId", null).get();
        if (null != aWSAccessId) {
            return aWSAccessId.trim();
        } else {
            return null;
        }
    }

    @Override
    public String getAWSSecretKey() {
        String aWSSecretKey = configInstance.getStringProperty(namespace + "awsSecretKey", null).get();
        if (null != aWSSecretKey) {
            return aWSSecretKey.trim();
        } else {
            return null;
        }
    }

    @Override
    public int getEIPBindRebindRetries() {
        return configInstance.getIntProperty(namespace + "eipBindRebindRetries", 3).get();
    }

    @Override
    public int getEIPBindingRetryIntervalMsWhenUnbound() {
        return configInstance.getIntProperty(namespace + "eipBindRebindRetryIntervalMsWhenUnbound", (1 * 60 * 1000)).get();
    }

    @Override
    public int getEIPBindingRetryIntervalMs() {
        return configInstance.getIntProperty(namespace + "eipBindRebindRetryIntervalMs", (5 * 60 * 1000)).get();
    }

    @Override
    public boolean shouldEnableSelfPreservation() {
        return configInstance.getBooleanProperty(namespace + "enableSelfPreservation", true).get();
    }

    @Override
    public int getPeerEurekaNodesUpdateIntervalMs() {
        return configInstance.getIntProperty(namespace + "peerEurekaNodesUpdateIntervalMs", (10 * 60 * 1000)).get();
    }

    @Override
    public int getRenewalThresholdUpdateIntervalMs() {
        return configInstance.getIntProperty(namespace + "renewalThresholdUpdateIntervalMs", (15 * 60 * 1000)).get();
    }

    @Override
    public double getRenewalPercentThreshold() {
        return configInstance.getDoubleProperty(namespace + "renewalPercentThreshold", 0.85).get();
    }

    @Override
    public boolean shouldEnableReplicatedRequestCompression() {
        return configInstance.getBooleanProperty(namespace + "enableReplicatedRequestCompression", false).get();
    }

    @Override
    public int getNumberOfReplicationRetries() {
        return configInstance.getIntProperty(namespace + "numberOfReplicationRetries", 5).get();
    }

    @Override
    public int getPeerEurekaStatusRefreshTimeIntervalMs() {
        return configInstance.getIntProperty(namespace + "peerEurekaStatusRefreshTimeIntervalMs", (30 * 1000)).get();
    }

    @Override
    public int getWaitTimeInMsWhenSyncEmpty() {
        return configInstance.getIntProperty(namespace + "waitTimeInMsWhenSyncEmpty", (1000 * 60 * 5)).get();
    }

    @Override
    public int getPeerNodeConnectTimeoutMs() {
        return configInstance.getIntProperty(namespace + "peerNodeConnectTimeoutMs", 1000).get();
    }

    @Override
    public int getPeerNodeReadTimeoutMs() {
        return configInstance.getIntProperty(namespace + "peerNodeReadTimeoutMs", 5000).get();
    }

    @Override
    public int getPeerNodeTotalConnections() {
        return configInstance.getIntProperty(namespace + "peerNodeTotalConnections", 1000).get();
    }

    @Override
    public int getPeerNodeTotalConnectionsPerHost() {
        return configInstance.getIntProperty(namespace + "peerNodeTotalConnectionsPerHost", 500).get();
    }

    @Override
    public int getPeerNodeConnectionIdleTimeoutSeconds() {
        return configInstance.getIntProperty(namespace + "peerNodeConnectionIdleTimeoutSeconds", 30).get();
    }

    @Override
    public long getRetentionTimeInMSInDeltaQueue() {
        return configInstance.getLongProperty(namespace + "retentionTimeInMSInDeltaQueue", (3 * 60 * 1000)).get();
    }

    @Override
    public long getDeltaRetentionTimerIntervalInMs() {
        return configInstance.getLongProperty(namespace + "deltaRetentionTimerIntervalInMs", (30 * 1000)).get();
    }

    @Override
    public long getEvictionIntervalTimerInMs() {
        return configInstance.getLongProperty(namespace + "evictionIntervalTimerInMs", (60 * 1000)).get();
    }

    @Override
    public int getASGQueryTimeoutMs() {
        return configInstance.getIntProperty(namespace + "asgQueryTimeoutMs", 300).get();
    }

    @Override
    public long getASGUpdateIntervalMs() {
        return configInstance.getIntProperty(namespace + "asgUpdateIntervalMs", (5 * 60 * 1000)).get();
    }

    @Override
    public long getASGCacheExpiryTimeoutMs() {
        return configInstance.getIntProperty(namespace + "asgCacheExpiryTimeoutMs", (10 * 60 * 1000)).get();
    }

    @Override
    public long getResponseCacheAutoExpirationInSeconds() {
        return configInstance.getIntProperty(namespace + "responseCacheAutoExpirationInSeconds", 180).get();
    }

    @Override
    public long getResponseCacheUpdateIntervalMs() {
        return configInstance.getIntProperty(namespace + "responseCacheUpdateIntervalMs", (30 * 1000)).get();
    }

    @Override
    public boolean shouldUseReadOnlyResponseCache() {
        return configInstance.getBooleanProperty(namespace + "shouldUseReadOnlyResponseCache", true).get();
    }

    @Override
    public boolean shouldDisableDelta() {
        return configInstance.getBooleanProperty(namespace + "disableDelta", false).get();
    }

    @Override
    public long getMaxIdleThreadInMinutesAgeForStatusReplication() {
        return configInstance.getLongProperty(namespace + "maxIdleThreadAgeInMinutesForStatusReplication", 10).get();
    }

    @Override
    public int getMinThreadsForStatusReplication() {
        return configInstance.getIntProperty(namespace + "minThreadsForStatusReplication", 1).get();
    }

    @Override
    public int getMaxThreadsForStatusReplication() {
        return configInstance.getIntProperty(namespace + "maxThreadsForStatusReplication", 1).get();
    }

    @Override
    public int getMaxElementsInStatusReplicationPool() {
        return configInstance.getIntProperty(namespace + "maxElementsInStatusReplicationPool", 10000).get();
    }

    @Override
    public boolean shouldSyncWhenTimestampDiffers() {
        return configInstance.getBooleanProperty(namespace + "syncWhenTimestampDiffers", true).get();
    }

    @Override
    public int getRegistrySyncRetries() {
        return configInstance.getIntProperty(namespace + "numberRegistrySyncRetries", 5).get();
    }

    @Override
    public long getRegistrySyncRetryWaitMs() {
        return configInstance.getIntProperty(namespace + "registrySyncRetryWaitMs", 30 * 1000).get();
    }

    @Override
    public int getMaxElementsInPeerReplicationPool() {
        return configInstance.getIntProperty(namespace + "maxElementsInPeerReplicationPool", 10000).get();
    }

    @Override
    public long getMaxIdleThreadAgeInMinutesForPeerReplication() {
        return configInstance.getIntProperty(namespace + "maxIdleThreadAgeInMinutesForPeerReplication", 15).get();
    }

    @Override
    public int getMinThreadsForPeerReplication() {
        return configInstance.getIntProperty(namespace + "minThreadsForPeerReplication", 5).get();
    }

    @Override
    public int getMaxThreadsForPeerReplication() {
        return configInstance.getIntProperty(namespace + "maxThreadsForPeerReplication", 20).get();
    }

    @Override
    public int getMaxTimeForReplication() {
        return configInstance.getIntProperty(namespace + "maxTimeForReplication", TIME_TO_WAIT_FOR_REPLICATION).get();
    }

    @Override
    public boolean shouldPrimeAwsReplicaConnections() {
        return configInstance.getBooleanProperty(namespace + "primeAwsReplicaConnections", true).get();
    }

    @Override
    public boolean shouldDisableDeltaForRemoteRegions() {
        return configInstance.getBooleanProperty(namespace + "disableDeltaForRemoteRegions", false).get();
    }

    @Override
    public int getRemoteRegionConnectTimeoutMs() {
        return configInstance.getIntProperty(namespace + "remoteRegionConnectTimeoutMs", 2000).get();
    }

    @Override
    public int getRemoteRegionReadTimeoutMs() {
        return configInstance.getIntProperty(namespace + "remoteRegionReadTimeoutMs", 5000).get();
    }

    @Override
    public int getRemoteRegionTotalConnections() {
        return configInstance.getIntProperty(
                namespace + "remoteRegionTotalConnections", 1000).get();
    }

    @Override
    public int getRemoteRegionTotalConnectionsPerHost() {
        return configInstance.getIntProperty(namespace + "remoteRegionTotalConnectionsPerHost", 500).get();
    }

    @Override
    public int getRemoteRegionConnectionIdleTimeoutSeconds() {
        return configInstance.getIntProperty(namespace + "remoteRegionConnectionIdleTimeoutSeconds", 30).get();
    }

    @Override
    public boolean shouldGZipContentFromRemoteRegion() {
        return configInstance.getBooleanProperty(namespace + "remoteRegion.gzipContent", true).get();
    }

    @Override
    public Map<String, String> getRemoteRegionUrlsWithName() {
        String propName = namespace + "remoteRegionUrlsWithName";
        String remoteRegionUrlWithNameString = configInstance.getStringProperty(propName, null).get();
        if (null == remoteRegionUrlWithNameString) {
            return Collections.emptyMap();
        }

        String[] remoteRegionUrlWithNamePairs = remoteRegionUrlWithNameString.split(",");
        Map<String, String> toReturn = new HashMap<String, String>(remoteRegionUrlWithNamePairs.length);

        final String pairSplitChar = ";";
        for (String remoteRegionUrlWithNamePair : remoteRegionUrlWithNamePairs) {
            String[] pairSplit = remoteRegionUrlWithNamePair.split(pairSplitChar);
            if (pairSplit.length < 2) {
                logger.error("Error reading eureka remote region urls from property {}. "
                                + "Invalid entry {} for remote region url. The entry must contain region name and url "
                                + "separated by a {}. Ignoring this entry.",
                        new String[]{propName, remoteRegionUrlWithNamePair, pairSplitChar});
            } else {
                String regionName = pairSplit[0];
                String regionUrl = pairSplit[1];
                if (pairSplit.length > 2) {
                    StringBuilder regionUrlAssembler = new StringBuilder();
                    for (int i = 1; i < pairSplit.length; i++) {
                        if (regionUrlAssembler.length() != 0) {
                            regionUrlAssembler.append(pairSplitChar);
                        }
                        regionUrlAssembler.append(pairSplit[i]);
                    }
                    regionUrl = regionUrlAssembler.toString();
                }
                toReturn.put(regionName, regionUrl);
            }
        }
        return toReturn;
    }

    @Override
    public String[] getRemoteRegionUrls() {
        String remoteRegionUrlString = configInstance.getStringProperty(
                namespace + "remoteRegionUrls", null).get();
        String[] remoteRegionUrl = null;
        if (remoteRegionUrlString != null) {
            remoteRegionUrl = remoteRegionUrlString.split(",");
        }
        return remoteRegionUrl;
    }

    @Override
    public Set<String> getRemoteRegionAppWhitelist( String regionName) {
        if (null == regionName) {
            regionName = "global";
        } else {
            regionName = regionName.trim().toLowerCase();
        }
        DynamicStringProperty appWhiteListProp =
                configInstance.getStringProperty(namespace + "remoteRegion." + regionName + ".appWhiteList", null);
        if (null == appWhiteListProp || null == appWhiteListProp.get()) {
            return null;
        } else {
            String appWhiteListStr = appWhiteListProp.get();
            String[] whitelistEntries = appWhiteListStr.split(",");
            return new HashSet<String>(Arrays.asList(whitelistEntries));
        }
    }

    @Override
    public int getRemoteRegionRegistryFetchInterval() {
        return configInstance.getIntProperty(namespace + "remoteRegion.registryFetchIntervalInSeconds", 30).get();
    }

    @Override
    public int getRemoteRegionFetchThreadPoolSize() {
        return configInstance.getIntProperty(namespace + "remoteRegion.fetchThreadPoolSize", 20).get();
    }

    @Override
    public String getRemoteRegionTrustStore() {
        return configInstance.getStringProperty(namespace + "remoteRegion.trustStoreFileName", "").get();

    }

    @Override
    public String getRemoteRegionTrustStorePassword() {
        return configInstance.getStringProperty(namespace + "remoteRegion.trustStorePassword", "changeit").get();
    }

    @Override
    public boolean disableTransparentFallbackToOtherRegion() {
        return configInstance.getBooleanProperty(namespace + "remoteRegion.disable.transparent.fallback", false).get();
    }

    @Override
    public boolean shouldBatchReplication() {
        return configInstance.getBooleanProperty(namespace + "shouldBatchReplication", false).get();
    }

    @Override
    public boolean shouldLogIdentityHeaders() {
        return configInstance.getBooleanProperty(namespace + "auth.shouldLogIdentityHeaders", true).get();
    }

    @Override
    public String getJsonCodecName() {
        return configInstance.getStringProperty(namespace + "jsonCodecName", null).get();
    }

    @Override
    public String getXmlCodecName() {
        return configInstance.getStringProperty(namespace + "xmlCodecName", null).get();
    }

    @Override
    public boolean isRateLimiterEnabled() {
        return rateLimiterEnabled.get();
    }

    @Override
    public boolean isRateLimiterThrottleStandardClients() {
        return rateLimiterThrottleStandardClients.get();
    }

    @Override
    public Set<String> getRateLimiterPrivilegedClients() {
        return rateLimiterPrivilegedClients.get();
    }

    @Override
    public int getRateLimiterBurstSize() {
        return rateLimiterBurstSize.get();
    }

    @Override
    public int getRateLimiterRegistryFetchAverageRate() {
        return rateLimiterRegistryFetchAverageRate.get();
    }

    @Override
    public int getRateLimiterFullFetchAverageRate() {
        return rateLimiterFullFetchAverageRate.get();
    }

    @Override
    public String getListAutoScalingGroupsRoleName() {
        return listAutoScalingGroupsRoleName.get();
    }

    @Override
    public int getRoute53BindRebindRetries() {
        return configInstance.getIntProperty(namespace + "route53BindRebindRetries", 3).get();

    }

    @Override
    public int getRoute53BindingRetryIntervalMs() {
        return configInstance.getIntProperty(namespace + "route53BindRebindRetryIntervalMs", (5 * 60 * 1000)).get();
    }

    @Override
    public long getRoute53DomainTTL() {
        return configInstance.getLongProperty(namespace + "route53DomainTTL", 30l).get();
    }
//
//    @Override
//    public AwsBindingStrategy getBindingStrategy() {
//        return AwsBindingStrategy.valueOf(configInstance.getStringProperty(namespace + "awsBindingStrategy", AwsBindingStrategy.EIP.name()).get().toUpperCase());
//    }

    @Override
    public String getExperimental(String name) {
        return configInstance.getStringProperty(namespace + "experimental." + name, null).get();
    }

    @Override
    public int getHealthStatusMinNumberOfAvailablePeers() {
        return configInstance.getIntProperty(namespace + "minAvailableInstancesForPeerReplication", -1).get();
    }
}

