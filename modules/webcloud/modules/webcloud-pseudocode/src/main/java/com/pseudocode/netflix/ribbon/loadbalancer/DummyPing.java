package com.pseudocode.netflix.ribbon.loadbalancer;

import com.pseudocode.netflix.ribbon.core.client.config.IClientConfig;
import com.pseudocode.netflix.ribbon.loadbalancer.server.Server;

public class DummyPing extends AbstractLoadBalancerPing {

    public DummyPing() {
    }

    public boolean isAlive(Server server) {
        return true;
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
    }
}

