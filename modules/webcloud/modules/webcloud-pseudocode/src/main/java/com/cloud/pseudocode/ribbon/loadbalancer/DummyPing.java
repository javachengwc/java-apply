package com.cloud.pseudocode.ribbon.loadbalancer;

import com.cloud.pseudocode.ribbon.core.client.config.IClientConfig;

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

