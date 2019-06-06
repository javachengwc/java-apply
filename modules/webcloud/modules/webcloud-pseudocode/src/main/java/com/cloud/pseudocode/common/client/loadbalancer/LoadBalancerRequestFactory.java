package com.cloud.pseudocode.common.client.loadbalancer;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

import java.util.List;

public class LoadBalancerRequestFactory {

    private LoadBalancerClient loadBalancer;
    private List<LoadBalancerRequestTransformer> transformers;

    public LoadBalancerRequestFactory(LoadBalancerClient loadBalancer,
                                      List<LoadBalancerRequestTransformer> transformers) {
        this.loadBalancer = loadBalancer;
        this.transformers = transformers;
    }

    public LoadBalancerRequestFactory(LoadBalancerClient loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    public LoadBalancerRequest<ClientHttpResponse> createRequest(final HttpRequest request,
                                                                 final byte[] body, final ClientHttpRequestExecution execution) {
        return instance -> {
            HttpRequest serviceRequest = new ServiceRequestWrapper(request, instance, loadBalancer);
            if (transformers != null) {
                for (LoadBalancerRequestTransformer transformer : transformers) {
                    serviceRequest = transformer.transformRequest(serviceRequest, instance);
                }
            }
            return execution.execute(serviceRequest, body);
        };
    }

}
