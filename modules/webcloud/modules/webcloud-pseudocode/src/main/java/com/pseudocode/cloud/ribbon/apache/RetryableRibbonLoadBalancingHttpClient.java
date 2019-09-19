package com.pseudocode.cloud.ribbon.apache;


import java.net.URI;

import com.pseudocode.cloud.commons.client.ServiceInstance;
import com.pseudocode.cloud.commons.client.loadbalancer.*;
import com.pseudocode.cloud.ribbon.RibbonProperties;
import com.pseudocode.cloud.ribbon.ServerIntrospector;
import com.pseudocode.netflix.ribbon.core.client.RequestSpecificRetryHandler;
import com.pseudocode.netflix.ribbon.core.client.RetryHandler;
import com.pseudocode.netflix.ribbon.core.client.config.IClientConfig;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.http.HttpRequest;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryListener;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.NoBackOffPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.util.UriComponentsBuilder;

//如果工程classpath中存在spring-retry，那么zuul在初始化的时候就不会创建 RibbonLoadBalancingHttpClient
//而是创建 RetryableRibbonLoadBalancingHttpClient
public class RetryableRibbonLoadBalancingHttpClient extends RibbonLoadBalancingHttpClient {

    private LoadBalancedRetryFactory loadBalancedRetryFactory;

    public RetryableRibbonLoadBalancingHttpClient(CloseableHttpClient delegate,
                                                  IClientConfig config, ServerIntrospector serverIntrospector,
                                                  LoadBalancedRetryFactory loadBalancedRetryFactory) {
        super(delegate, config, serverIntrospector);
        this.loadBalancedRetryFactory = loadBalancedRetryFactory;
    }

    @Override
    public RibbonApacheHttpResponse execute(final RibbonApacheHttpRequest request, final IClientConfig configOverride) throws Exception {
        final RequestConfig.Builder builder = RequestConfig.custom();
        IClientConfig config = configOverride != null ? configOverride : this.config;
        RibbonProperties ribbon = RibbonProperties.from(config);
        builder.setConnectTimeout(ribbon.connectTimeout(this.connectTimeout));
        builder.setSocketTimeout(ribbon.readTimeout(this.readTimeout));
        builder.setRedirectsEnabled(ribbon.isFollowRedirects(this.followRedirects));

        final RequestConfig requestConfig = builder.build();
        final LoadBalancedRetryPolicy retryPolicy = loadBalancedRetryFactory.createRetryPolicy(this.getClientName(), this);

        //RetryCallback的实现类
        RetryCallback<RibbonApacheHttpResponse, Exception> retryCallback = context -> {
            //on retries the policy will choose the server and set it in the context
            //extract the server and update the request being made
            RibbonApacheHttpRequest newRequest = request;
            if (context instanceof LoadBalancedRetryContext) {
                ServiceInstance service = ((LoadBalancedRetryContext) context).getServiceInstance();
                validateServiceInstance(service);
                if (service != null) {
                    //Reconstruct the request URI using the host and port set in the retry context
                    newRequest = newRequest.withNewUri(UriComponentsBuilder.newInstance().host(service.getHost())
                            .scheme(service.getUri().getScheme()).userInfo(newRequest.getURI().getUserInfo())
                            .port(service.getPort()).path(newRequest.getURI().getPath())
                            .query(newRequest.getURI().getQuery()).fragment(newRequest.getURI().getFragment())
                            .build().encode().toUri());
                }
            }
            newRequest = getSecureRequest(newRequest, configOverride);
            HttpUriRequest httpUriRequest = newRequest.toRequest(requestConfig);
            final HttpResponse httpResponse = RetryableRibbonLoadBalancingHttpClient.this.delegate.execute(httpUriRequest);
            if (retryPolicy.retryableStatusCode(httpResponse.getStatusLine().getStatusCode())) {
                throw new HttpClientStatusCodeException(RetryableRibbonLoadBalancingHttpClient.this.clientName,
                        httpResponse, HttpClientUtils.createEntity(httpResponse), httpUriRequest.getURI());
            }
            return new RibbonApacheHttpResponse(httpResponse, httpUriRequest.getURI());
        };
        LoadBalancedRecoveryCallback<RibbonApacheHttpResponse, HttpResponse> recoveryCallback = new LoadBalancedRecoveryCallback<RibbonApacheHttpResponse, HttpResponse>() {
            @Override
            protected RibbonApacheHttpResponse createResponse(HttpResponse response, URI uri) {
                return new RibbonApacheHttpResponse(response, uri);
            }
        };
        return this.executeWithRetry(request, retryPolicy, retryCallback, recoveryCallback);
    }

    @Override
    public boolean isClientRetryable(ContextAwareRequest request) {
        return request!= null && isRequestRetryable(request);
    }

    private boolean isRequestRetryable(ContextAwareRequest request) {
        if (request.getContext() == null || request.getContext().getRetryable() == null) {
            return true;
        }
        return request.getContext().getRetryable();
    }

    private RibbonApacheHttpResponse executeWithRetry(RibbonApacheHttpRequest request, LoadBalancedRetryPolicy retryPolicy,
                                                      RetryCallback<RibbonApacheHttpResponse, Exception> callback,
                                                      RecoveryCallback<RibbonApacheHttpResponse> recoveryCallback) throws Exception {
        RetryTemplate retryTemplate = new RetryTemplate();
        //是否可retry
        boolean retryable = isRequestRetryable(request);
        retryTemplate.setRetryPolicy(retryPolicy == null || !retryable ? new NeverRetryPolicy()
                : new RetryPolicy(request, retryPolicy, this, this.getClientName()));
        BackOffPolicy backOffPolicy = loadBalancedRetryFactory.createBackOffPolicy(this.getClientName());
        retryTemplate.setBackOffPolicy(backOffPolicy == null ? new NoBackOffPolicy() : backOffPolicy);
        RetryListener[] retryListeners = this.loadBalancedRetryFactory.createRetryListeners(this.getClientName());
        if (retryListeners != null && retryListeners.length != 0) {
            retryTemplate.setListeners(retryListeners);
        }
        return retryTemplate.execute(callback, recoveryCallback);
    }

    @Override
    public RequestSpecificRetryHandler getRequestSpecificRetryHandler(RibbonApacheHttpRequest request, IClientConfig requestConfig) {
        return new RequestSpecificRetryHandler(false, false, RetryHandler.DEFAULT, null);
    }

    static class RetryPolicy extends InterceptorRetryPolicy {
        public RetryPolicy(HttpRequest request, LoadBalancedRetryPolicy policy,
                           ServiceInstanceChooser serviceInstanceChooser, String serviceName) {
            super(request, policy, serviceInstanceChooser, serviceName);
        }
    }
}
