package com.pseudocode.netflix.eureka.client.discovery.shared;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
//import com.netflix.discovery.InstanceRegionChecker;
import com.pseudocode.netflix.eureka.client.appinfo.InstanceInfo;
import com.pseudocode.netflix.eureka.client.appinfo.InstanceInfo.InstanceStatus;
import com.pseudocode.netflix.eureka.client.discovery.EurekaClientConfig;

@JsonRootName("application")
public class Application {

    private static Random shuffleRandom = new Random();

    @Override
    public String toString() {
        return "Application [name=" + name + ", isDirty=" + isDirty
                + ", instances=" + instances + ", shuffledInstances="
                + shuffledInstances + ", instancesMap=" + instancesMap + "]";
    }

    private String name;

    private volatile boolean isDirty = false;

    private final Set<InstanceInfo> instances;

    private final AtomicReference<List<InstanceInfo>> shuffledInstances;

    private final Map<String, InstanceInfo> instancesMap;

    public Application() {
        instances = new LinkedHashSet<InstanceInfo>();
        instancesMap = new ConcurrentHashMap<String, InstanceInfo>();
        shuffledInstances = new AtomicReference<List<InstanceInfo>>();
    }

    public Application(String name) {
        this();
        this.name = name;
    }

    @JsonCreator
    public Application(
            @JsonProperty("name") String name,
            @JsonProperty("instance") List<InstanceInfo> instances) {
        this(name);
        for (InstanceInfo instanceInfo : instances) {
            addInstance(instanceInfo);
        }
    }

    public void addInstance(InstanceInfo i) {
        instancesMap.put(i.getId(), i);
        synchronized (instances) {
            instances.remove(i);
            instances.add(i);
            isDirty = true;
        }
    }

    public void removeInstance(InstanceInfo i) {
        removeInstance(i, true);
    }

    @JsonProperty("instance")
    public List<InstanceInfo> getInstances() {
        return Optional.ofNullable(shuffledInstances.get()).orElseGet(this::getInstancesAsIsFromEureka);
    }

    @JsonIgnore
    public List<InstanceInfo> getInstancesAsIsFromEureka() {
        synchronized (instances) {
            return new ArrayList<InstanceInfo>(this.instances);
        }
    }

    public InstanceInfo getByInstanceId(String id) {
        return instancesMap.get(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name =name;
    }

    public int size() {
        return instances.size();
    }

    public void shuffleAndStoreInstances(boolean filterUpInstances) {
        //_shuffleAndStoreInstances(filterUpInstances, false, null, null, null);
    }
//
//    public void shuffleAndStoreInstances(Map<String, Applications> remoteRegionsRegistry,
//                                         EurekaClientConfig clientConfig, InstanceRegionChecker instanceRegionChecker) {
//        _shuffleAndStoreInstances(clientConfig.shouldFilterOnlyUpInstances(), true, remoteRegionsRegistry, clientConfig,
//                instanceRegionChecker);
//    }
//
//    private void _shuffleAndStoreInstances(boolean filterUpInstances, boolean indexByRemoteRegions,
//                                          Map<String, Applications> remoteRegionsRegistry,
//                                          EurekaClientConfig clientConfig,
//                                          InstanceRegionChecker instanceRegionChecker) {
//        List<InstanceInfo> instanceInfoList;
//        synchronized (instances) {
//            instanceInfoList = new ArrayList<InstanceInfo>(instances);
//        }
//        boolean remoteIndexingActive = indexByRemoteRegions && null != instanceRegionChecker && null != clientConfig
//                && null != remoteRegionsRegistry;
//        if (remoteIndexingActive || filterUpInstances) {
//            Iterator<InstanceInfo> it = instanceInfoList.iterator();
//            while (it.hasNext()) {
//                InstanceInfo instanceInfo = it.next();
//                if (filterUpInstances && InstanceStatus.UP != instanceInfo.getStatus()) {
//                    it.remove();
//                } else if (remoteIndexingActive) {
//                    String instanceRegion = instanceRegionChecker.getInstanceRegion(instanceInfo);
//                    if (!instanceRegionChecker.isLocalRegion(instanceRegion)) {
//                        Applications appsForRemoteRegion = remoteRegionsRegistry.get(instanceRegion);
//                        if (null == appsForRemoteRegion) {
//                            appsForRemoteRegion = new Applications();
//                            remoteRegionsRegistry.put(instanceRegion, appsForRemoteRegion);
//                        }
//
//                        Application remoteApp =
//                                appsForRemoteRegion.getRegisteredApplications(instanceInfo.getAppName());
//                        if (null == remoteApp) {
//                            remoteApp = new Application(instanceInfo.getAppName());
//                            appsForRemoteRegion.addApplication(remoteApp);
//                        }
//
//                        remoteApp.addInstance(instanceInfo);
//                        this.removeInstance(instanceInfo, false);
//                        it.remove();
//                    }
//                }
//            }
//
//        }
//        Collections.shuffle(instanceInfoList, shuffleRandom);
//        this.shuffledInstances.set(instanceInfoList);
//    }

    private void removeInstance(InstanceInfo i, boolean markAsDirty) {
        instancesMap.remove(i.getId());
        synchronized (instances) {
            instances.remove(i);
            if (markAsDirty) {
                isDirty = true;
            }
        }
    }
}

