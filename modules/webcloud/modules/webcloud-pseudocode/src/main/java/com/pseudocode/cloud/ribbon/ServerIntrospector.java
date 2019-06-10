package com.pseudocode.cloud.ribbon;

import com.pseudocode.netflix.ribbon.loadbalancer.server.Server;

import java.util.Map;

public interface ServerIntrospector {

    boolean isSecure(Server server);

    Map<String, String> getMetadata(Server server);
}
