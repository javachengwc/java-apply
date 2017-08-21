package com.component.microservice.loadbalance;

import com.component.microservice.IServer;

import java.util.List;

public interface ILoadBalancer {

    public void addServer(IServer server);

    public IServer choose(Object key);

    public List<IServer> getAll();
}
