package com.pseudocode.netflix.ribbon.loadbalancer.client;

import com.pseudocode.netflix.ribbon.core.client.IClientConfigAware;
import com.pseudocode.netflix.ribbon.loadbalancer.server.Server;

public interface IPrimeConnection extends IClientConfigAware {

    public boolean connect(Server server, String uriPath) throws Exception;

}