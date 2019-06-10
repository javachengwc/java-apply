package com.pseudocode.netflix.ribbon.loadbalancer.server;

import com.pseudocode.netflix.ribbon.loadbalancer.LoadBalancerStats;

public abstract class AbstractServerListFilter<T extends Server> implements ServerListFilter<T> {

    private volatile LoadBalancerStats stats;

    public void setLoadBalancerStats(LoadBalancerStats stats) {
        this.stats = stats;
    }

    public LoadBalancerStats getLoadBalancerStats() {
        return stats;
    }

}
