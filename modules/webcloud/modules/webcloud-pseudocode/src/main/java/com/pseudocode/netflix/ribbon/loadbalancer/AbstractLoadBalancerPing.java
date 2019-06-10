package com.pseudocode.netflix.ribbon.loadbalancer;

import com.pseudocode.netflix.ribbon.core.client.IClientConfigAware;
import com.pseudocode.netflix.ribbon.loadbalancer.server.Server;

public abstract class AbstractLoadBalancerPing implements IPing, IClientConfigAware {

    AbstractLoadBalancer lb;

    @Override
    public boolean isAlive(Server server) {
        return true;
    }

    public void setLoadBalancer(AbstractLoadBalancer lb){
        this.lb = lb;
    }

    public AbstractLoadBalancer getLoadBalancer(){
        return lb;
    }

}
