package com.pseudocode.cloud.commons.client.loadbalancer;

import com.pseudocode.cloud.commons.client.ServiceInstance;

public interface LoadBalancerRequest<T> {

    T apply(ServiceInstance var1) throws Exception;
}