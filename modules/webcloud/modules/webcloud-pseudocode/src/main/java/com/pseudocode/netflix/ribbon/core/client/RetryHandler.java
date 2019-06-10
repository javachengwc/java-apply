package com.pseudocode.netflix.ribbon.core.client;

public interface RetryHandler {

    public static final RetryHandler DEFAULT = new DefaultLoadBalancerRetryHandler();

    public boolean isRetriableException(Throwable e, boolean sameServer);

    public boolean isCircuitTrippingException(Throwable e);

    public int getMaxRetriesOnSameServer();

    public int getMaxRetriesOnNextServer();
}
