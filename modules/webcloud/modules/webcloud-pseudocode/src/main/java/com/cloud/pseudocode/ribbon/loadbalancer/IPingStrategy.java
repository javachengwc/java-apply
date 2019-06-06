package com.cloud.pseudocode.ribbon.loadbalancer;

public interface IPingStrategy {

    boolean[] pingServers(IPing ping, Server[] servers);
}
