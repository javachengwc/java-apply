package com.pseudocode.netflix.ribbon.loadbalancer.client;

import java.net.URI;

import com.pseudocode.netflix.ribbon.core.client.*;
import com.pseudocode.netflix.ribbon.core.client.config.CommonClientConfigKey;
import com.pseudocode.netflix.ribbon.core.client.config.IClientConfig;
import com.pseudocode.netflix.ribbon.loadbalancer.ILoadBalancer;
import com.pseudocode.netflix.ribbon.loadbalancer.LoadBalancerContext;
import com.pseudocode.netflix.ribbon.loadbalancer.reactive.LoadBalancerCommand;
import com.pseudocode.netflix.ribbon.loadbalancer.server.Server;
import rx.Observable;

public abstract class AbstractLoadBalancerAwareClient<S extends ClientRequest, T extends IResponse> extends LoadBalancerContext implements IClient<S, T>, IClientConfigAware {

    public AbstractLoadBalancerAwareClient(ILoadBalancer lb) {
        super(lb);
    }

    public AbstractLoadBalancerAwareClient(ILoadBalancer lb, IClientConfig clientConfig) {
        super(lb, clientConfig);
    }

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

    //使用负载均衡的方式去执行请求
    public T executeWithLoadBalancer(final S request, final IClientConfig requestConfig) throws ClientException {
        LoadBalancerCommand<T> command = buildLoadBalancerCommand(request, requestConfig);

        try {
            return command.submit(
                    new ServerOperation<T>() {
                        @Override
                        public Observable<T> call(Server server) {
                            URI finalUri = reconstructURIWithServer(server, request.getUri());
                            S requestForServer = (S) request.replaceUri(finalUri);
                            try {
                                return Observable.just(AbstractLoadBalancerAwareClient.this.execute(requestForServer, requestConfig));
                            }
                            catch (Exception e) {
                                return Observable.error(e);
                            }
                        }
                    })
                    .toBlocking()
                    .single();
        } catch (Exception e) {
            Throwable t = e.getCause();
            if (t instanceof ClientException) {
                throw (ClientException) t;
            } else {
                throw new ClientException(e);
            }
        }

    }

    public abstract RequestSpecificRetryHandler getRequestSpecificRetryHandler(S request, IClientConfig requestConfig);

    protected LoadBalancerCommand<T> buildLoadBalancerCommand(final S request, final IClientConfig config) {
        RequestSpecificRetryHandler handler = getRequestSpecificRetryHandler(request, config);
        LoadBalancerCommand.Builder<T> builder = LoadBalancerCommand.<T>builder()
                .withLoadBalancerContext(this)
                .withRetryHandler(handler)
                .withLoadBalancerURI(request.getUri());
        customizeLoadBalancerCommandBuilder(request, config, builder);
        return builder.build();
    }

    protected void customizeLoadBalancerCommandBuilder(final S request, final IClientConfig config,
                                                       final LoadBalancerCommand.Builder<T> builder) {
    }

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

