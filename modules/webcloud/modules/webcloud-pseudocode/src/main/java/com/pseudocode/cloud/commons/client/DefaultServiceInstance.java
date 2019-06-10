package com.pseudocode.cloud.commons.client;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class DefaultServiceInstance implements ServiceInstance {

    private final String serviceId;

    private final String host;

    private final int port;

    private final boolean secure;

    private final Map<String, String> metadata;

    public DefaultServiceInstance(String serviceId, String host, int port, boolean secure,
                                  Map<String, String> metadata) {
        this.serviceId = serviceId;
        this.host = host;
        this.port = port;
        this.secure = secure;
        this.metadata = metadata;
    }

    public DefaultServiceInstance(String serviceId, String host, int port,
                                  boolean secure) {
        this(serviceId, host, port, secure, new LinkedHashMap<String, String>());
    }

    @Override
    public URI getUri() {
        return getUri(this);
    }

    @Override
    public Map<String, String> getMetadata() {
        return this.metadata;
    }

    public static URI getUri(ServiceInstance instance) {
        String scheme = (instance.isSecure()) ? "https" : "http";
        String uri = String.format("%s://%s:%s", scheme, instance.getHost(),
                instance.getPort());
        return URI.create(uri);
    }

    @Override
    public String getServiceId() {
        return serviceId;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public boolean isSecure() {
        return secure;
    }

    @Override
    public String toString() {
        return "DefaultServiceInstance{" +
                "serviceId='" + serviceId + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", secure=" + secure +
                ", metadata=" + metadata +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultServiceInstance that = (DefaultServiceInstance) o;
        return port == that.port &&
                secure == that.secure &&
                Objects.equals(serviceId, that.serviceId) &&
                Objects.equals(host, that.host) &&
                Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceId, host, port, secure, metadata);
    }
}
