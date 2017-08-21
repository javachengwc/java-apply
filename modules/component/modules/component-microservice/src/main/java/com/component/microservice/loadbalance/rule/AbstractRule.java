package com.component.microservice.loadbalance.rule;

import com.component.microservice.loadbalance.ILoadBalancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractRule implements IRule {

    protected Logger logger= LoggerFactory.getLogger(getClass());

    protected ILoadBalancer loadBalancer;

    public ILoadBalancer getLoadBalancer() {
        return loadBalancer;
    }

    public void setLoadBalancer(ILoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }
}
