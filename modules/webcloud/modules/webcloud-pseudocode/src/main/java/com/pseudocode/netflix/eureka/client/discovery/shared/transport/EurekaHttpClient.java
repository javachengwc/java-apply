package com.pseudocode.netflix.eureka.client.discovery.shared.transport;


import com.pseudocode.netflix.eureka.client.appinfo.InstanceInfo;
import com.pseudocode.netflix.eureka.client.appinfo.InstanceInfo.InstanceStatus;
import com.pseudocode.netflix.eureka.client.discovery.shared.Application;
import com.pseudocode.netflix.eureka.client.discovery.shared.Applications;

//Low level Eureka HTTP client API
public interface EurekaHttpClient {

    //服务注册
    EurekaHttpResponse<Void> register(InstanceInfo info);

    EurekaHttpResponse<Void> cancel(String appName, String id);

    //心跳，服务续约
    EurekaHttpResponse<InstanceInfo> sendHeartBeat(String appName, String id, InstanceInfo info, InstanceStatus overriddenStatus);

    EurekaHttpResponse<Void> statusUpdate(String appName, String id, InstanceStatus newStatus, InstanceInfo info);

    EurekaHttpResponse<Void> deleteStatusOverride(String appName, String id, InstanceInfo info);

    //全量获取某区域下的注册应用
    EurekaHttpResponse<Applications> getApplications(String... regions);

    //从注册中心获取区域下的应用
    EurekaHttpResponse<Applications> getDelta(String... regions);

    EurekaHttpResponse<Applications> getVip(String vipAddress, String... regions);

    EurekaHttpResponse<Applications> getSecureVip(String secureVipAddress, String... regions);

    EurekaHttpResponse<Application> getApplication(String appName);

    EurekaHttpResponse<InstanceInfo> getInstance(String appName, String id);

    //获取实例信息
    EurekaHttpResponse<InstanceInfo> getInstance(String id);

    void shutdown();
}

