package com.pseudocode.cloud.ribbon;

import com.pseudocode.netflix.ribbon.loadbalancer.server.Server;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Map;

public class DefaultServerIntrospector implements ServerIntrospector {

    private ServerIntrospectorProperties serverIntrospectorProperties = new ServerIntrospectorProperties();

    public DefaultServerIntrospector() {
    }

    @Autowired(
            required = false
    )
    public void setServerIntrospectorProperties(ServerIntrospectorProperties serverIntrospectorProperties) {
        this.serverIntrospectorProperties = serverIntrospectorProperties;
    }

    public boolean isSecure(Server server) {
        return this.serverIntrospectorProperties.getSecurePorts().contains(server.getPort());
    }

    public Map<String, String> getMetadata(Server server) {
        return Collections.emptyMap();
    }
}
