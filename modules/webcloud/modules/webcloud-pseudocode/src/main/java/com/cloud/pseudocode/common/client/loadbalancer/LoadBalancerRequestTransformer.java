package com.cloud.pseudocode.common.client.loadbalancer;

import com.cloud.pseudocode.common.client.ServiceInstance;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpRequest;

@Order(LoadBalancerRequestTransformer.DEFAULT_ORDER)
public interface LoadBalancerRequestTransformer {

    public static final int DEFAULT_ORDER = 0;

    HttpRequest transformRequest(HttpRequest request, ServiceInstance instance);
}

