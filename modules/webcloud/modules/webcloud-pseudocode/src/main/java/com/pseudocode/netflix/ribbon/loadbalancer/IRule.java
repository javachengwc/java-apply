package com.pseudocode.netflix.ribbon.loadbalancer;

import com.pseudocode.netflix.ribbon.loadbalancer.server.Server;

//负载均衡的处理规则,一个算法组件，决定在服务列表中使用哪一个
public interface IRule{

    public Server choose(Object key);

    public void setLoadBalancer(ILoadBalancer lb);

    public ILoadBalancer getLoadBalancer();
}
