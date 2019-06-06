package com.cloud.pseudocode.ribbon.loadbalancer;

import com.cloud.pseudocode.ribbon.core.client.IClientConfigAware;

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
