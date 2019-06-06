package com.cloud.pseudocode.ribbon.loadbalancer;

import java.util.List;

public interface ServerList<T extends Server> {

    public List<T> getInitialListOfServers();

    public List<T> getUpdatedListOfServers();

}
