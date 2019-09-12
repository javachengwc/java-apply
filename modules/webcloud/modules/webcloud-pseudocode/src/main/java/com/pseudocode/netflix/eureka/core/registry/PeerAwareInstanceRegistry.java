package com.pseudocode.netflix.eureka.core.registry;


import com.pseudocode.netflix.eureka.client.appinfo.InstanceInfo;
import com.pseudocode.netflix.eureka.core.cluster.PeerEurekaNodes;

import java.util.List;

//PeerAware应用对象注册表接口，提供 Eureka-Server 集群内注册信息的同步服务
public interface PeerAwareInstanceRegistry extends InstanceRegistry {

    void init(PeerEurekaNodes peerEurekaNodes) throws Exception;

    boolean shouldAllowAccess(boolean remoteRegionRequired);

    void register(InstanceInfo info, boolean isReplication);

    //void statusUpdate(final String asgName, final ASGResource.ASGStatus newStatus, final boolean isReplication);
}

