package com.cloud.pseudocode.common.client.loadbalancer;

import com.cloud.pseudocode.common.client.ServiceInstance;

public interface LoadBalancerRequest<T> {

    T apply(ServiceInstance var1) throws Exception;
}