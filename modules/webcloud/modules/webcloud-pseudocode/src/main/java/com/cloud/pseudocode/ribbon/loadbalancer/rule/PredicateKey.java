package com.cloud.pseudocode.ribbon.loadbalancer.rule;

import com.cloud.pseudocode.ribbon.loadbalancer.Server;

public class PredicateKey {
    private Object loadBalancerKey;
    private Server server;

    public PredicateKey(Object loadBalancerKey, Server server) {
        this.loadBalancerKey = loadBalancerKey;
        this.server = server;
    }

    public PredicateKey(Server server) {
        this(null, server);
    }

    public final Object getLoadBalancerKey() {
        return loadBalancerKey;
    }

    public final Server getServer() {
        return server;
    }
}
