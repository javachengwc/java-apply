package com.pseudocode.cloud.common.client.loadbalancer;

import com.pseudocode.cloud.common.client.ServiceInstance;

public interface LoadBalancerRequest<T> {

    T apply(ServiceInstance var1) throws Exception;
}