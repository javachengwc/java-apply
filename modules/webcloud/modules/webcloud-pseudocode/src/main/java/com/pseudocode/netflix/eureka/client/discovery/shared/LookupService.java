package com.pseudocode.netflix.eureka.client.discovery.shared;


import java.util.List;

import com.pseudocode.netflix.eureka.client.appinfo.InstanceInfo;

//查找服务接口
public interface LookupService<T> {

    Application getApplication(String appName);

    Applications getApplications();

    List<InstanceInfo> getInstancesById(String id);

    InstanceInfo getNextServerFromEureka(String virtualHostname, boolean secure);
}

