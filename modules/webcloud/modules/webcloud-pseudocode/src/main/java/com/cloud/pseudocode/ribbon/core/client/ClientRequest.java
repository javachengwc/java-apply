package com.cloud.pseudocode.ribbon.core.client;


import com.cloud.pseudocode.ribbon.core.client.config.IClientConfig;

import java.net.URI;

public class ClientRequest implements Cloneable {
    protected URI uri;
    protected Object loadBalancerKey = null;
    protected Boolean isRetriable = null;
    protected IClientConfig overrideConfig;

    public ClientRequest() {
    }

    public ClientRequest(URI uri) {
        this.uri = uri;
    }

    @Deprecated
    public ClientRequest(URI uri, Object loadBalancerKey, boolean isRetriable, IClientConfig overrideConfig) {
        this.uri = uri;
        this.loadBalancerKey = loadBalancerKey;
        this.isRetriable = isRetriable;
        this.overrideConfig = overrideConfig;
    }

    public ClientRequest(URI uri, Object loadBalancerKey, boolean isRetriable) {
        this.uri = uri;
        this.loadBalancerKey = loadBalancerKey;
        this.isRetriable = isRetriable;
    }

    public ClientRequest(ClientRequest request) {
        this.uri = request.uri;
        this.loadBalancerKey = request.loadBalancerKey;
        this.overrideConfig = request.overrideConfig;
        this.isRetriable = request.isRetriable;
    }

    public final URI getUri() {
        return this.uri;
    }

    protected final ClientRequest setUri(URI uri) {
        this.uri = uri;
        return this;
    }

    public final Object getLoadBalancerKey() {
        return this.loadBalancerKey;
    }

    protected final ClientRequest setLoadBalancerKey(Object loadBalancerKey) {
        this.loadBalancerKey = loadBalancerKey;
        return this;
    }

    public boolean isRetriable() {
        return Boolean.TRUE.equals(this.isRetriable);
    }

    protected final ClientRequest setRetriable(boolean isRetriable) {
        this.isRetriable = isRetriable;
        return this;
    }

    @Deprecated
    public final IClientConfig getOverrideConfig() {
        return this.overrideConfig;
    }

    @Deprecated
    protected final ClientRequest setOverrideConfig(IClientConfig overrideConfig) {
        this.overrideConfig = overrideConfig;
        return this;
    }

    public ClientRequest replaceUri(URI newURI) {
        ClientRequest req;
        try {
            req = (ClientRequest)this.clone();
        } catch (CloneNotSupportedException var4) {
            req = new ClientRequest(this);
        }

        req.uri = newURI;
        return req;
    }
}
