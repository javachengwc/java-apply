package com.pseudocode.netflix.eureka.core.registry;


//import com.netflix.appinfo.ApplicationInfoManager;
import com.pseudocode.netflix.commons.util.Pair;
import com.pseudocode.netflix.eureka.client.appinfo.InstanceInfo;
import com.pseudocode.netflix.eureka.client.appinfo.InstanceInfo.InstanceStatus;
import com.pseudocode.netflix.eureka.client.discovery.shared.Application;
import com.pseudocode.netflix.eureka.client.discovery.shared.Applications;
import com.pseudocode.netflix.eureka.client.discovery.shared.LookupService;
import com.pseudocode.netflix.eureka.core.lease.LeaseManager;

import java.util.List;
import java.util.Map;

//应用实例注册表接口
public interface InstanceRegistry extends LeaseManager<InstanceInfo>, LookupService<String> {

    //void openForTraffic(ApplicationInfoManager applicationInfoManager, int count);

    void shutdown();

    @Deprecated
    void storeOverriddenStatusIfRequired(String id, InstanceStatus overriddenStatus);

    void storeOverriddenStatusIfRequired(String appName, String id, InstanceStatus overriddenStatus);

    boolean statusUpdate(String appName, String id, InstanceStatus newStatus,
                         String lastDirtyTimestamp, boolean isReplication);

    boolean deleteStatusOverride(String appName, String id, InstanceStatus newStatus,
                                 String lastDirtyTimestamp, boolean isReplication);

    Map<String, InstanceStatus> overriddenInstanceStatusesSnapshot();

    Applications getApplicationsFromLocalRegionOnly();

    List<Application> getSortedApplications();

    Application getApplication(String appName, boolean includeRemoteRegion);

    InstanceInfo getInstanceByAppAndId(String appName, String id);

    InstanceInfo getInstanceByAppAndId(String appName, String id, boolean includeRemoteRegions);

    void clearRegistry();

    void initializedResponseCache();

    ResponseCache getResponseCache();

    long getNumOfRenewsInLastMin();

    int getNumOfRenewsPerMinThreshold();

    int isBelowRenewThresold();

    List<Pair<Long, String>> getLastNRegisteredInstances();

    List<Pair<Long, String>> getLastNCanceledInstances();

    boolean isLeaseExpirationEnabled();

    boolean isSelfPreservationModeEnabled();

}

