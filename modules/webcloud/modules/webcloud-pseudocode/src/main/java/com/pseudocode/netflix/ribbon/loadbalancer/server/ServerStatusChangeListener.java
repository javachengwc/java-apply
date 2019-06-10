package com.pseudocode.netflix.ribbon.loadbalancer.server;

import java.util.Collection;

public interface ServerStatusChangeListener {

    public void serverStatusChanged(Collection<Server> servers);

}
