package com.cloud.pseudocode.ribbon;

import com.cloud.pseudocode.ribbon.loadbalancer.Server;

import java.util.Map;

public interface ServerIntrospector {

    boolean isSecure(Server server);

    Map<String, String> getMetadata(Server server);
}
