package com.pseudocode.netflix.eureka.client.discovery.shared;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.pseudocode.netflix.eureka.client.appinfo.InstanceInfo;
import com.pseudocode.netflix.eureka.client.appinfo.InstanceInfo.InstanceStatus;

@JsonRootName("applications")
public class Applications {
    private static class VipIndexSupport {
        final AbstractQueue<InstanceInfo> instances = new ConcurrentLinkedQueue<>();
        final AtomicLong roundRobinIndex = new AtomicLong(0);
        final AtomicReference<List<InstanceInfo>> vipList = new AtomicReference<List<InstanceInfo>>(Collections.emptyList());

        public AtomicLong getRoundRobinIndex() {
            return roundRobinIndex;
        }

        public AtomicReference<List<InstanceInfo>> getVipList() {
            return vipList;
        }
    }

    private static final String STATUS_DELIMITER = "_";

    private String appsHashCode;
    private Long versionDelta;
    private final AbstractQueue<Application> applications;
    private final Map<String, Application> appNameApplicationMap;
    private final Map<String, VipIndexSupport> virtualHostNameAppMap;
    private final Map<String, VipIndexSupport> secureVirtualHostNameAppMap;

    public Applications() {
        this(null, -1L, Collections.emptyList());
    }

    @JsonCreator
    public Applications(@JsonProperty("appsHashCode") String appsHashCode,
                        @JsonProperty("versionDelta") Long versionDelta,
                        @JsonProperty("application") List<Application> registeredApplications) {
        this.applications = new ConcurrentLinkedQueue<Application>();
        this.appNameApplicationMap = new ConcurrentHashMap<String, Application>();
        this.virtualHostNameAppMap = new ConcurrentHashMap<String, VipIndexSupport>();
        this.secureVirtualHostNameAppMap = new ConcurrentHashMap<String, VipIndexSupport>();
        this.appsHashCode = appsHashCode;
        this.versionDelta = versionDelta;

        for (Application app : registeredApplications) {
            this.addApplication(app);
        }
    }

    public void addApplication(Application app) {
        appNameApplicationMap.put(app.getName().toUpperCase(Locale.ROOT), app);
        addInstancesToVIPMaps(app, this.virtualHostNameAppMap, this.secureVirtualHostNameAppMap);
        applications.add(app);
    }

    @JsonProperty("application")
    public List<Application> getRegisteredApplications() {
        return new ArrayList<Application>(this.applications);
    }

    public Application getRegisteredApplications(String appName) {
        return appNameApplicationMap.get(appName.toUpperCase(Locale.ROOT));
    }

    public List<InstanceInfo> getInstancesByVirtualHostName(String virtualHostName) {
        return Optional.ofNullable(this.virtualHostNameAppMap.get(virtualHostName.toUpperCase(Locale.ROOT)))
                .map(VipIndexSupport::getVipList)
                .map(AtomicReference::get)
                .orElseGet(Collections::emptyList);
    }

    public List<InstanceInfo> getInstancesBySecureVirtualHostName(String secureVirtualHostName) {
        return Optional.ofNullable(this.secureVirtualHostNameAppMap.get(secureVirtualHostName.toUpperCase(Locale.ROOT)))
                .map(VipIndexSupport::getVipList)
                .map(AtomicReference::get)
                .orElseGet(Collections::emptyList);
    }

    public int size() {
        return applications.stream().mapToInt(Application::size).sum();
    }

    @Deprecated
    public void setVersion(Long version) {
        this.versionDelta = version;
    }

    @Deprecated
    @JsonIgnore // Handled directly due to legacy name formatting
    public Long getVersion() {
        return this.versionDelta;
    }

    public void setAppsHashCode(String hashCode) {
        this.appsHashCode = hashCode;
    }

    @JsonIgnore // Handled directly due to legacy name formatting
    public String getAppsHashCode() {
        return this.appsHashCode;
    }

    @JsonIgnore
    public String getReconcileHashCode() {
        TreeMap<String, AtomicInteger> instanceCountMap = new TreeMap<String, AtomicInteger>();
        populateInstanceCountMap(instanceCountMap);
        return getReconcileHashCode(instanceCountMap);
    }

    public void populateInstanceCountMap(Map<String, AtomicInteger> instanceCountMap) {
        for (Application app : this.getRegisteredApplications()) {
            for (InstanceInfo info : app.getInstancesAsIsFromEureka()) {
                AtomicInteger instanceCount = instanceCountMap.computeIfAbsent(info.getStatus().name(),
                        k -> new AtomicInteger(0));
                instanceCount.incrementAndGet();
            }
        }
    }

    public static String getReconcileHashCode(Map<String, AtomicInteger> instanceCountMap) {
        StringBuilder reconcileHashCode = new StringBuilder(75);
        for (Map.Entry<String, AtomicInteger> mapEntry : instanceCountMap.entrySet()) {
            reconcileHashCode.append(mapEntry.getKey()).append(STATUS_DELIMITER).append(mapEntry.getValue().get())
                    .append(STATUS_DELIMITER);
        }
        return reconcileHashCode.toString();
    }

    public void shuffleInstances(boolean filterUpInstances) {
        //shuffleInstances(filterUpInstances, false, null, null, null);
    }

//    public void shuffleAndIndexInstances(Map<String, Applications> remoteRegionsRegistry,
//                                         EurekaClientConfig clientConfig, InstanceRegionChecker instanceRegionChecker) {
//        shuffleInstances(clientConfig.shouldFilterOnlyUpInstances(), true, remoteRegionsRegistry, clientConfig,instanceRegionChecker);
//    }

    public AtomicLong getNextIndex(String virtualHostname, boolean secure) {
        Map<String, VipIndexSupport> index = (secure) ? secureVirtualHostNameAppMap : virtualHostNameAppMap;
        return Optional.ofNullable(index.get(virtualHostname.toUpperCase(Locale.ROOT)))
                .map(VipIndexSupport::getRoundRobinIndex)
                .orElse(null);
    }

    private void shuffleAndFilterInstances(Map<String, VipIndexSupport> srcMap, boolean filterUpInstances) {

        Random shuffleRandom = new Random();
        for (Map.Entry<String, VipIndexSupport> entries : srcMap.entrySet()) {
            VipIndexSupport vipIndexSupport = entries.getValue();
            AbstractQueue<InstanceInfo> vipInstances = vipIndexSupport.instances;
            final List<InstanceInfo> filteredInstances;
            if (filterUpInstances) {
                filteredInstances = vipInstances.stream().filter(ii -> ii.getStatus() == InstanceStatus.UP)
                        .collect(Collectors.toCollection(() -> new ArrayList<>(vipInstances.size())));
            } else {
                filteredInstances = new ArrayList<InstanceInfo>(vipInstances);
            }
            Collections.shuffle(filteredInstances, shuffleRandom);
            vipIndexSupport.vipList.set(filteredInstances);
            vipIndexSupport.roundRobinIndex.set(0);
        }
    }

    private void addInstanceToMap(InstanceInfo info, String vipAddresses, Map<String, VipIndexSupport> vipMap) {
        if (vipAddresses != null) {
            String[] vipAddressArray = vipAddresses.toUpperCase(Locale.ROOT).split(",");
            for (String vipAddress : vipAddressArray) {
                VipIndexSupport vis = vipMap.computeIfAbsent(vipAddress, k -> new VipIndexSupport());
                vis.instances.add(info);
            }
        }
    }

    private void addInstancesToVIPMaps(Application app, Map<String, VipIndexSupport> virtualHostNameAppMap,
                                       Map<String, VipIndexSupport> secureVirtualHostNameAppMap) {
        // Check and add the instances to the their respective virtual host name
        // mappings
        for (InstanceInfo info : app.getInstances()) {
            String vipAddresses = info.getVIPAddress();
            if (vipAddresses != null) {
                addInstanceToMap(info, vipAddresses, virtualHostNameAppMap);
            }

            String secureVipAddresses = info.getSecureVipAddress();
            if (secureVipAddresses != null) {
                addInstanceToMap(info, secureVipAddresses, secureVirtualHostNameAppMap);
            }
        }
    }
}

