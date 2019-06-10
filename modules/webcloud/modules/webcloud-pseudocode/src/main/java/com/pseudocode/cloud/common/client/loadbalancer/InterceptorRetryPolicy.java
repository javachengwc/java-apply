package com.pseudocode.cloud.common.client.loadbalancer;

import org.springframework.http.HttpRequest;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryPolicy;

public class InterceptorRetryPolicy implements RetryPolicy {

    private HttpRequest request;
    private LoadBalancedRetryPolicy policy;
    private ServiceInstanceChooser serviceInstanceChooser;
    private String serviceName;

    public InterceptorRetryPolicy(HttpRequest request, LoadBalancedRetryPolicy policy,
                                  ServiceInstanceChooser serviceInstanceChooser, String serviceName) {
        this.request = request;
        this.policy = policy;
        this.serviceInstanceChooser = serviceInstanceChooser;
        this.serviceName = serviceName;
    }

    @Override
    public boolean canRetry(RetryContext context) {
        LoadBalancedRetryContext lbContext = (LoadBalancedRetryContext)context;
        if(lbContext.getRetryCount() == 0  && lbContext.getServiceInstance() == null) {
            //We haven't even tried to make the request yet so return true so we do
            lbContext.setServiceInstance(serviceInstanceChooser.choose(serviceName));
            return true;
        }
        return policy.canRetryNextServer(lbContext);
    }

    @Override
    public RetryContext open(RetryContext parent) {
        return new LoadBalancedRetryContext(parent, request);
    }


    @Override
    public void close(RetryContext context) {
        policy.close((LoadBalancedRetryContext)context);
    }


    @Override
    public void registerThrowable(RetryContext context, Throwable throwable) {
        LoadBalancedRetryContext lbContext = (LoadBalancedRetryContext) context;
        //this is important as it registers the last exception in the context and also increases the retry count
        lbContext.registerThrowable(throwable);
        //let the policy know about the exception as well
        policy.registerThrowable(lbContext, throwable);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InterceptorRetryPolicy that = (InterceptorRetryPolicy) o;

        if (!request.equals(that.request)) return false;
        if (!policy.equals(that.policy)) return false;
        if (!serviceInstanceChooser.equals(that.serviceInstanceChooser)) return false;
        return serviceName.equals(that.serviceName);

    }

    @Override
    public int hashCode() {
        int result = request.hashCode();
        result = 31 * result + policy.hashCode();
        result = 31 * result + serviceInstanceChooser.hashCode();
        result = 31 * result + serviceName.hashCode();
        return result;
    }
}
