package com.cloud.pseudocode.common.client.loadbalancer;

import com.cloud.pseudocode.common.client.ServiceInstance;
import org.springframework.http.HttpRequest;
import org.springframework.retry.RetryContext;
import org.springframework.retry.context.RetryContextSupport;

public class LoadBalancedRetryContext extends RetryContextSupport {

    private HttpRequest request;
    private ServiceInstance serviceInstance;

    public LoadBalancedRetryContext(RetryContext parent, HttpRequest request) {
        super(parent);
        this.request = request;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public ServiceInstance getServiceInstance() {
        return serviceInstance;
    }

    public void setServiceInstance(ServiceInstance serviceInstance) {
        this.serviceInstance = serviceInstance;
    }
}
