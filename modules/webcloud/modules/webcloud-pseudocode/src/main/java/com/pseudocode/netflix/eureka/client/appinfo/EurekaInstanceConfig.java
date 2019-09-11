package com.pseudocode.netflix.eureka.client.appinfo;


import java.util.Map;

//import com.google.inject.ImplementedBy;

//@ImplementedBy(CloudInstanceConfig.class)
//Eureka 应用实例配置
public interface EurekaInstanceConfig {

    String getInstanceId();

    String getAppname();

    String getAppGroupName();

    boolean isInstanceEnabledOnit();

    int getNonSecurePort();

    int getSecurePort();

    boolean isNonSecurePortEnabled();

    boolean getSecurePortEnabled();

    //表示eureka client发送心跳给server端的频率。默认30秒
    //租约续约频率，单位：秒。应用不断通过按照该频率发送心跳给 Eureka-Server 以达到续约的作用
    int getLeaseRenewalIntervalInSeconds();

    //Eureka server至上一次收到client的心跳之后，等待下一次心跳的超时时间，也就是服务失效时间,契约过期时间,默认为90秒
    int getLeaseExpirationDurationInSeconds();

    String getVirtualHostName();

    String getSecureVirtualHostName();

    String getASGName();

    String getHostName(boolean refresh);

    Map<String, String> getMetadataMap();

    //数据中心信息
    DataCenterInfo getDataCenterInfo();

    String getIpAddress();

    //状态页path,默认/info
    String getStatusPageUrlPath();

    String getStatusPageUrl();

    String getHomePageUrlPath();

    String getHomePageUrl();

    //健康监控页path,默认/health
    String getHealthCheckUrlPath();

    String getHealthCheckUrl();

    String getSecureHealthCheckUrl();

    String[] getDefaultAddressResolutionOrder();

    //配置命名空间,默认eureka,如配置文件中配置项eureka.port=8080，每个属性最前面的eureka即是配置命名空间，一般情况无需修改。
    String getNamespace();

}

