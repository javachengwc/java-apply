package com.cloud.pseudocode.ribbon.loadbalancer;

import java.util.Collection;

public interface ServerStatusChangeListener {

    public void serverStatusChanged(Collection<Server> servers);

}
