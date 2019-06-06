package com.cloud.pseudocode.ribbon.loadbalancer;

import java.util.List;

public interface ServerListFilter<T extends Server> {

    public List<T> getFilteredListOfServers(List<T> servers);

}
