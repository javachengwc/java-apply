package com.cloud.pseudocode.ribbon.loadbalancer.client;

import com.cloud.pseudocode.ribbon.core.client.*;
import com.cloud.pseudocode.ribbon.core.client.config.CommonClientConfigKey;
import com.cloud.pseudocode.ribbon.core.client.config.IClientConfig;
import com.cloud.pseudocode.ribbon.loadbalancer.ILoadBalancer;
import com.cloud.pseudocode.ribbon.loadbalancer.LoadBalancerContext;

import java.net.URI;

public abstract class AbstractLoadBalancerAwareClient<S extends ClientRequest, T extends IResponse> extends LoadBalancerContext implements IClient<S, T>, IClientConfigAware {

    public AbstractLoadBalancerAwareClient(ILoadBalancer lb) {
        super(lb);
    }

    public AbstractLoadBalancerAwareClient(ILoadBalancer lb, IClientConfig clientConfig) {
        super(lb, clientConfig);
    }

    @Deprecated
    protected boolean isCircuitBreakerException(Throwable e) {
        if (getRetryHandler() != null) {
            return getRetryHandler().isCircuitTrippingException(e);
        }
        return false;
    }

    @Deprecated
    protected boolean isRetriableException(Throwable e) {
        if (getRetryHandler() != null) {
            return getRetryHandler().isRetriableException(e, true);
        }
        return false;
    }

    public T executeWithLoadBalancer(S request) throws ClientException {
        return executeWithLoadBalancer(request, null);
    }

    public T executeWithLoadBalancer(final S request, final IClientConfig requestConfig) throws ClientException {
        //..........
        return null;
    }

    public abstract RequestSpecificRetryHandler getRequestSpecificRetryHandler(S request, IClientConfig requestConfig);

    @Deprecated
    protected boolean isRetriable(S request) {
        if (request.isRetriable()) {
            return true;
        } else {
            boolean retryOkayOnOperation = okToRetryOnAllOperations;
            IClientConfig overriddenClientConfig = request.getOverrideConfig();
            if (overriddenClientConfig != null) {
                retryOkayOnOperation = overriddenClientConfig.getPropertyAsBoolean(CommonClientConfigKey.RequestSpecificRetryOn, okToRetryOnAllOperations);
            }
            return retryOkayOnOperation;
        }
    }

}
