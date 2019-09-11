package com.pseudocode.cloud.commons.client.discovery;

import com.pseudocode.cloud.commons.client.ServiceInstance;

import java.util.List;

public interface DiscoveryClient {

    String description();

    List<ServiceInstance> getInstances(String serviceId);

    List<String> getServices();

}
