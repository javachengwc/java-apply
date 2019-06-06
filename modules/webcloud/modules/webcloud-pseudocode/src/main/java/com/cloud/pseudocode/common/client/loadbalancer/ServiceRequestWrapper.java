package com.cloud.pseudocode.common.client.loadbalancer;

import com.cloud.pseudocode.common.client.ServiceInstance;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.support.HttpRequestWrapper;

import java.net.URI;

public class ServiceRequestWrapper extends HttpRequestWrapper {

    private final ServiceInstance instance;

    private final LoadBalancerClient loadBalancer;

    public ServiceRequestWrapper(HttpRequest request, ServiceInstance instance,
                                 LoadBalancerClient loadBalancer) {
        super(request);
        this.instance = instance;
        this.loadBalancer = loadBalancer;
    }

    @Override
    public URI getURI() {
        URI uri = this.loadBalancer.reconstructURI(
                this.instance, getRequest().getURI());
        return uri;
    }
}
