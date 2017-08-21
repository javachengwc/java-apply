package com.component.microservice.loadbalance.rule;

import com.component.microservice.IServer;
import com.component.microservice.loadbalance.ILoadBalancer;

/**
 * 负载均衡规则
 */
public interface IRule{

    public void setLoadBalancer(ILoadBalancer lb);

    public ILoadBalancer getLoadBalancer();

    public IServer choose(Object key);

}