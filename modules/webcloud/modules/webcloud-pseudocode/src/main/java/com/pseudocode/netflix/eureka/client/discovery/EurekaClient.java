package com.pseudocode.netflix.eureka.client.discovery;


import java.util.List;
import java.util.Set;

//import com.google.inject.ImplementedBy;
//import com.netflix.appinfo.ApplicationInfoManager;
//import com.netflix.appinfo.HealthCheckCallback;
//import com.netflix.appinfo.HealthCheckHandler;
import com.pseudocode.netflix.eureka.client.appinfo.InstanceInfo;
import com.pseudocode.netflix.eureka.client.discovery.shared.Applications;
import com.pseudocode.netflix.eureka.client.discovery.shared.LookupService;

//@ImplementedBy(DiscoveryClient.class)
public interface EurekaClient extends LookupService {

    public Applications getApplicationsForARegion(String region);

    public Applications getApplications(String serviceUrl);

    public List<InstanceInfo> getInstancesByVipAddress(String vipAddress, boolean secure);

    public List<InstanceInfo> getInstancesByVipAddress(String vipAddress, boolean secure, String region);

    public List<InstanceInfo> getInstancesByVipAddressAndAppName(String vipAddress, String appName, boolean secure);

    public Set<String> getAllKnownRegions();

    public InstanceInfo.InstanceStatus getInstanceRemoteStatus();

    @Deprecated
    public List<String> getDiscoveryServiceUrls(String zone);

    @Deprecated
    public List<String> getServiceUrlsFromConfig(String instanceZone, boolean preferSameZone);

    @Deprecated
    public List<String> getServiceUrlsFromDNS(String instanceZone, boolean preferSameZone);
//
//    @Deprecated
//    public void registerHealthCheckCallback(HealthCheckCallback callback);
//
//    public void registerHealthCheck(HealthCheckHandler healthCheckHandler);
//
//    public void registerEventListener(EurekaEventListener eventListener);
//
//    public boolean unregisterEventListener(EurekaEventListener eventListener);
//
//    public HealthCheckHandler getHealthCheckHandler();

    public void shutdown();

    public EurekaClientConfig getEurekaClientConfig();

//    public ApplicationInfoManager getApplicationInfoManager();
}
