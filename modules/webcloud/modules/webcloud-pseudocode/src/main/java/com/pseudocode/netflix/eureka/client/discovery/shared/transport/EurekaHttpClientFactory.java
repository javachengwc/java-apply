package com.pseudocode.netflix.eureka.client.discovery.shared.transport;

public interface EurekaHttpClientFactory {

    EurekaHttpClient newClient();

    void shutdown();

}
