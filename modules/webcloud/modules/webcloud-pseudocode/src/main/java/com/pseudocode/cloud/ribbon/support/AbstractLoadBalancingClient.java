package com.pseudocode.cloud.ribbon.support;

import com.pseudocode.cloud.commons.client.ServiceInstance;
import com.pseudocode.cloud.commons.client.loadbalancer.ServiceInstanceChooser;
import com.pseudocode.cloud.ribbon.RibbonLoadBalancerClient;
import com.pseudocode.cloud.ribbon.RibbonProperties;
import com.pseudocode.cloud.ribbon.ServerIntrospector;
import com.pseudocode.netflix.ribbon.core.client.ClientException;
import com.pseudocode.netflix.ribbon.core.client.IResponse;
import com.pseudocode.netflix.ribbon.core.client.RequestSpecificRetryHandler;
import com.pseudocode.netflix.ribbon.core.client.RetryHandler;
import com.pseudocode.netflix.ribbon.core.client.config.IClientConfig;
import com.pseudocode.netflix.ribbon.loadbalancer.client.AbstractLoadBalancerAwareClient;
import com.pseudocode.netflix.ribbon.loadbalancer.reactive.LoadBalancerCommand;
import com.pseudocode.netflix.ribbon.loadbalancer.server.Server;
import static com.pseudocode.cloud.ribbon.RibbonClientConfiguration.DEFAULT_CONNECT_TIMEOUT;
import static com.pseudocode.cloud.ribbon.RibbonClientConfiguration.DEFAULT_READ_TIMEOUT;

public abstract class AbstractLoadBalancingClient<S extends ContextAwareRequest, T extends IResponse, D> extends
        AbstractLoadBalancerAwareClient<S, T> implements ServiceInstanceChooser {

    protected int connectTimeout;

    protected int readTimeout;

    protected boolean secure;

    protected boolean followRedirects;

    protected boolean okToRetryOnAllOperations;

    protected final D delegate;
    protected final IClientConfig config;
    protected final ServerIntrospector serverIntrospector;

    public boolean isClientRetryable(ContextAwareRequest request) {
        return false;
    }

    protected AbstractLoadBalancingClient(IClientConfig config, ServerIntrospector serverIntrospector) {
        super(null);
        this.delegate = createDelegate(config);
        this.config = config;
        this.serverIntrospector = serverIntrospector;
        this.setRetryHandler(RetryHandler.DEFAULT);
        initWithNiwsConfig(config);
    }

    protected AbstractLoadBalancingClient(D delegate, IClientConfig config, ServerIntrospector serverIntrospector) {
        super(null);
        this.delegate = delegate;
        this.config = config;
        this.serverIntrospector = serverIntrospector;
        this.setRetryHandler(RetryHandler.DEFAULT);
        initWithNiwsConfig(config);
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        super.initWithNiwsConfig(clientConfig);
        RibbonProperties ribbon = RibbonProperties.from(clientConfig);
        this.connectTimeout = ribbon.connectTimeout(DEFAULT_CONNECT_TIMEOUT);
        this.readTimeout = ribbon.readTimeout(DEFAULT_READ_TIMEOUT);
        this.secure = ribbon.isSecure();
        this.followRedirects = ribbon.isFollowRedirects();
        this.okToRetryOnAllOperations = ribbon.isOkToRetryOnAllOperations();
    }

    protected abstract D createDelegate(IClientConfig config);

    public D getDelegate() {
        return this.delegate;
    }

    @Override
    public RequestSpecificRetryHandler getRequestSpecificRetryHandler(
            final S request, final IClientConfig requestConfig) {
        if (this.okToRetryOnAllOperations) {
            return new RequestSpecificRetryHandler(true, true, this.getRetryHandler(),
                    requestConfig);
        }

        if (!request.getContext().getMethod().equals("GET")) {
            return new RequestSpecificRetryHandler(true, false, this.getRetryHandler(),
                    requestConfig);
        }
        else {
            return new RequestSpecificRetryHandler(true, true, this.getRetryHandler(),
                    requestConfig);
        }
    }

    protected boolean isSecure(final IClientConfig config) {
        if(config != null) {
            return RibbonProperties.from(config).isSecure(this.secure);
        }
        return this.secure;
    }

    @Override
    protected void customizeLoadBalancerCommandBuilder(S request, IClientConfig config, LoadBalancerCommand.Builder<T> builder) {
        if (request.getLoadBalancerKey() != null) {
            builder.withServerLocator(request.getLoadBalancerKey());
        }
    }

    @Override
    public ServiceInstance choose(String serviceId) {
        Server server = this.getLoadBalancer().chooseServer(serviceId);
        if (server != null) {
            return new RibbonLoadBalancerClient.RibbonServer(serviceId, server);
        }
        return null;
    }

    public void validateServiceInstance(ServiceInstance serviceInstance) throws ClientException {
        if (serviceInstance == null) {
            throw new ClientException("Load balancer does not have available server for client: " + clientName);
        } else if (serviceInstance.getHost() == null) {
            throw new ClientException("Invalid Server for: " + serviceInstance.getServiceId() + " null Host");
        }
    }
}
