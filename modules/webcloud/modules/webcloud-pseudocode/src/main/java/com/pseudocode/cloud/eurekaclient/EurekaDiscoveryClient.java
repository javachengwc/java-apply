package com.pseudocode.cloud.eurekaclient;


import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.pseudocode.cloud.commons.client.ServiceInstance;
import com.pseudocode.cloud.commons.client.discovery.DiscoveryClient;
import com.pseudocode.netflix.eureka.client.appinfo.EurekaInstanceConfig;
import com.pseudocode.netflix.eureka.client.discovery.EurekaClient;
import com.pseudocode.netflix.eureka.client.discovery.shared.Application;
import com.pseudocode.netflix.eureka.client.discovery.shared.Applications;
import org.springframework.util.Assert;

import com.pseudocode.netflix.eureka.client.appinfo.InstanceInfo;

//eureka客户端
public class EurekaDiscoveryClient implements DiscoveryClient {

    public static final String DESCRIPTION = "Spring Cloud Eureka Discovery Client";

    private final EurekaInstanceConfig config;

    private final EurekaClient eurekaClient;

    public EurekaDiscoveryClient(EurekaInstanceConfig config, EurekaClient eurekaClient) {
        this.config = config;
        this.eurekaClient = eurekaClient;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceId) {
        List<InstanceInfo> infos = this.eurekaClient.getInstancesByVipAddress(serviceId,
                false);
        List<ServiceInstance> instances = new ArrayList<>();
        for (InstanceInfo info : infos) {
            instances.add(new EurekaServiceInstance(info));
        }
        return instances;
    }

    public static class EurekaServiceInstance implements ServiceInstance {
        private InstanceInfo instance;

        public EurekaServiceInstance(InstanceInfo instance) {
            Assert.notNull(instance, "Service instance required");
            this.instance = instance;
        }

        public InstanceInfo getInstanceInfo() {
            return instance;
        }

        @Override
        public String getServiceId() {
            return this.instance.getAppName();
        }

        @Override
        public String getHost() {
            return this.instance.getHostName();
        }

        @Override
        public int getPort() {
            if (isSecure()) {
                return this.instance.getSecurePort();
            }
            return this.instance.getPort();
        }

        @Override
        public boolean isSecure() {
            // assume if secure is enabled, that is the default
           // return this.instance.isPortEnabled(SECURE);
            return true;
        }

        @Override
        public URI getUri() {
            //return DefaultServiceInstance.getUri(this);
            return null;
        }

        @Override
        public Map<String, String> getMetadata() {
            return this.instance.getMetadata();
        }
    }

    @Override
    public List<String> getServices() {
        Applications applications = this.eurekaClient.getApplications();
        if (applications == null) {
            return Collections.emptyList();
        }
        List<Application> registered = applications.getRegisteredApplications();
        List<String> names = new ArrayList<>();
        for (Application app : registered) {
            if (app.getInstances().isEmpty()) {
                continue;
            }
            names.add(app.getName().toLowerCase());

        }
        return names;
    }

}
