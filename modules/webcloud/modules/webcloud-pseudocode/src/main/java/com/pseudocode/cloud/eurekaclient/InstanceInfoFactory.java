package com.pseudocode.cloud.eurekaclient;


import java.util.Map;

import com.pseudocode.netflix.eureka.client.appinfo.EurekaInstanceConfig;
import com.pseudocode.netflix.eureka.client.appinfo.InstanceInfo;
import com.pseudocode.netflix.eureka.client.appinfo.LeaseInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//将EurekaInstanceConfig信息转换成InstanceInfo信息
public class InstanceInfoFactory {

    private static final Log log = LogFactory.getLog(InstanceInfoFactory.class);

    public InstanceInfo create(EurekaInstanceConfig config) {
        LeaseInfo.Builder leaseInfoBuilder = LeaseInfo.Builder.newBuilder()
                .setRenewalIntervalInSecs(config.getLeaseRenewalIntervalInSeconds())
                .setDurationInSecs(config.getLeaseExpirationDurationInSeconds());

        // Builder the instance information to be registered with eureka
        // server
        InstanceInfo.Builder builder = InstanceInfo.Builder.newBuilder();

        String namespace = config.getNamespace();
        if (!namespace.endsWith(".")) {
            namespace = namespace + ".";
        }
        builder.setNamespace(namespace).setAppName(config.getAppname())
                .setInstanceId(config.getInstanceId())
                .setAppGroupName(config.getAppGroupName())
                .setDataCenterInfo(config.getDataCenterInfo())
                .setIPAddr(config.getIpAddress()).setHostName(config.getHostName(false))
                .setPort(config.getNonSecurePort())
                .enablePort(InstanceInfo.PortType.UNSECURE,
                        config.isNonSecurePortEnabled())
                .setSecurePort(config.getSecurePort())
                .enablePort(InstanceInfo.PortType.SECURE, config.getSecurePortEnabled())
                .setVIPAddress(config.getVirtualHostName())
                .setSecureVIPAddress(config.getSecureVirtualHostName())
                .setHomePageUrl(config.getHomePageUrlPath(), config.getHomePageUrl())
                .setStatusPageUrl(config.getStatusPageUrlPath(),
                        config.getStatusPageUrl())
                .setHealthCheckUrls(config.getHealthCheckUrlPath(),
                        config.getHealthCheckUrl(), config.getSecureHealthCheckUrl())
                .setASGName(config.getASGName());

        // Start off with the STARTING state to avoid traffic
        if (!config.isInstanceEnabledOnit()) {
            InstanceInfo.InstanceStatus initialStatus = InstanceInfo.InstanceStatus.STARTING;
            if (log.isInfoEnabled()) {
                log.info("Setting initial instance status as: " + initialStatus);
            }
            builder.setStatus(initialStatus);
        }
        else {
            if (log.isInfoEnabled()) {
                log.info("Setting initial instance status as: "
                        + InstanceInfo.InstanceStatus.UP
                        + ". This may be too early for the instance to advertise itself as available. "
                        + "You would instead want to control this via a healthcheck handler.");
            }
        }

        // Add any user-specific metadata information
        for (Map.Entry<String, String> mapEntry : config.getMetadataMap().entrySet()) {
            String key = mapEntry.getKey();
            String value = mapEntry.getValue();
            // only add the metadata if the value is present
            if (value != null && !value.isEmpty()) {
                builder.add(key, value);
            }
        }

        InstanceInfo instanceInfo = builder.build();
        instanceInfo.setLeaseInfo(leaseInfoBuilder.build());
        return instanceInfo;
    }
}
