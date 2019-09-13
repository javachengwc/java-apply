package com.pseudocode.netflix.ribbon.loadbalancer;

import com.pseudocode.netflix.ribbon.loadbalancer.server.Server;

//检查服务实例操作的执行策略
public interface IPingStrategy {

    boolean[] pingServers(IPing ping, Server[] servers);
}
