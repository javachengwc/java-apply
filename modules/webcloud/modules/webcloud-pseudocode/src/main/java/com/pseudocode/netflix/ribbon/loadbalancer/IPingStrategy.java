package com.pseudocode.netflix.ribbon.loadbalancer;

import com.pseudocode.netflix.ribbon.loadbalancer.server.Server;

public interface IPingStrategy {

    boolean[] pingServers(IPing ping, Server[] servers);
}
