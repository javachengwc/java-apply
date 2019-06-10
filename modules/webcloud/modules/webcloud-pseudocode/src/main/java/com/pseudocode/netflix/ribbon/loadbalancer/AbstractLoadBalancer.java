package com.pseudocode.netflix.ribbon.loadbalancer;

import com.pseudocode.netflix.ribbon.loadbalancer.server.Server;

import java.util.List;

public abstract class AbstractLoadBalancer implements ILoadBalancer {

    public enum ServerGroup{
        ALL,
        STATUS_UP,
        STATUS_NOT_UP
    }

    public Server chooseServer() {
        return chooseServer(null);
    }

    public abstract List<Server> getServerList(ServerGroup serverGroup);

    public abstract LoadBalancerStats getLoadBalancerStats();
}
