package com.pseudocode.netflix.ribbon.loadbalancer;

import com.pseudocode.netflix.ribbon.loadbalancer.server.Server;

//检查服务是否可用
public interface IPing {

    public boolean isAlive(Server server);
}
