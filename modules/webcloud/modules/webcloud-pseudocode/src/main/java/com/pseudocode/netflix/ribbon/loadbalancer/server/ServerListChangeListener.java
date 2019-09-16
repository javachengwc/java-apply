package com.pseudocode.netflix.ribbon.loadbalancer.server;

import java.util.List;

public interface ServerListChangeListener {

    public void serverListChanged(List<Server> oldList, List<Server> newList);
}

