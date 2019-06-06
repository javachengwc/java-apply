package com.cloud.pseudocode.common.client.loadbalancer;

import org.springframework.retry.RetryListener;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.NoBackOffPolicy;

public interface LoadBalancedRetryFactory {

    default LoadBalancedRetryPolicy createRetryPolicy(String service, ServiceInstanceChooser serviceInstanceChooser) {
        return null;
    }

    default RetryListener[] createRetryListeners(String service) {
        return new RetryListener[0];
    }

    default BackOffPolicy createBackOffPolicy(String service) {
        return new NoBackOffPolicy();
    }
}

