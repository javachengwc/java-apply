package com.pseudocode.netflix.ribbon.loadbalancer.rule;

import com.pseudocode.netflix.ribbon.core.client.IClientConfigAware;
import com.pseudocode.netflix.ribbon.loadbalancer.ILoadBalancer;
import com.pseudocode.netflix.ribbon.loadbalancer.IRule;

public abstract class AbstractLoadBalancerRule implements IRule, IClientConfigAware {

    private ILoadBalancer lb;

    @Override
    public void setLoadBalancer(ILoadBalancer lb){
        this.lb = lb;
    }

    @Override
    public ILoadBalancer getLoadBalancer(){
        return lb;
    }
}
