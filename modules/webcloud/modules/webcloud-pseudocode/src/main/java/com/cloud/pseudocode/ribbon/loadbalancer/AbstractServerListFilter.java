package com.cloud.pseudocode.ribbon.loadbalancer;

public abstract class AbstractServerListFilter<T extends Server> implements ServerListFilter<T> {

    private volatile LoadBalancerStats stats;

    public void setLoadBalancerStats(LoadBalancerStats stats) {
        this.stats = stats;
    }

    public LoadBalancerStats getLoadBalancerStats() {
        return stats;
    }

}
