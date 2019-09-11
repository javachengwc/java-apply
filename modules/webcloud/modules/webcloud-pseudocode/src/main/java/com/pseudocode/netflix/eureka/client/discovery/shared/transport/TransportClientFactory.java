package com.pseudocode.netflix.eureka.client.discovery.shared.transport;

import com.pseudocode.netflix.eureka.client.discovery.shared.resolver.EurekaEndpoint;

public interface TransportClientFactory {

    EurekaHttpClient newClient(EurekaEndpoint serviceUrl);

    void shutdown();

}