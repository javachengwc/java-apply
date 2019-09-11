package com.pseudocode.netflix.eureka.client.discovery.shared.resolver;

public interface EurekaEndpoint extends Comparable<Object> {

    String getServiceUrl();

    @Deprecated
    String getHostName();

    String getNetworkAddress();

    int getPort();

    boolean isSecure();

    String getRelativeUri();

}
