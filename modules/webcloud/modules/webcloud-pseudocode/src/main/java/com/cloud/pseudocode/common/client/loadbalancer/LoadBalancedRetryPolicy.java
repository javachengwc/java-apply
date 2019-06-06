package com.cloud.pseudocode.common.client.loadbalancer;

public interface LoadBalancedRetryPolicy {

    public boolean canRetrySameServer(LoadBalancedRetryContext context);

    public boolean canRetryNextServer(LoadBalancedRetryContext context);

    public abstract void close(LoadBalancedRetryContext context);

    public abstract void registerThrowable(LoadBalancedRetryContext context, Throwable throwable);

    public boolean retryableStatusCode(int statusCode);
}
