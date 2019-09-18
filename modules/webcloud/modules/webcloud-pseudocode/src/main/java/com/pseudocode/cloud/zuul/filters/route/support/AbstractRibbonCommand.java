package com.pseudocode.cloud.zuul.filters.route.support;


import com.pseudocode.cloud.ribbon.RibbonClientConfiguration;
import com.pseudocode.cloud.ribbon.RibbonHttpResponse;
import com.pseudocode.cloud.ribbon.support.RibbonCommandContext;
import com.pseudocode.cloud.zuul.filters.ZuulProperties;
import com.pseudocode.cloud.zuul.filters.route.RibbonCommand;
import com.pseudocode.netflix.hystrix.core.HystrixCommand;
import com.pseudocode.netflix.hystrix.core.HystrixCommandKey;
import com.pseudocode.netflix.hystrix.core.HystrixCommandProperties;
import com.pseudocode.netflix.hystrix.core.HystrixThreadPoolKey;
import com.pseudocode.netflix.ribbon.core.client.ClientRequest;
import com.pseudocode.netflix.ribbon.core.client.config.DefaultClientConfigImpl;
import com.pseudocode.netflix.ribbon.core.client.config.IClientConfig;
import com.pseudocode.netflix.ribbon.core.client.config.IClientConfigKey;
import com.pseudocode.netflix.ribbon.httpclient.http.HttpResponse;
import com.pseudocode.netflix.ribbon.loadbalancer.client.AbstractLoadBalancerAwareClient;
import com.pseudocode.netflix.zuul.context.RequestContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.client.ClientHttpResponse;
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;

public abstract class AbstractRibbonCommand<LBC extends AbstractLoadBalancerAwareClient<RQ, RS>, RQ extends ClientRequest, RS extends HttpResponse>
        extends HystrixCommand<ClientHttpResponse> implements RibbonCommand {

    private static final Log LOGGER = LogFactory.getLog(AbstractRibbonCommand.class);
    protected final LBC client;
    protected RibbonCommandContext context;
    protected FallbackProvider zuulFallbackProvider;
    protected IClientConfig config;

    public AbstractRibbonCommand(LBC client, RibbonCommandContext context,
                                 ZuulProperties zuulProperties) {
        this("default", client, context, zuulProperties);
    }

    public AbstractRibbonCommand(String commandKey, LBC client,
                                 RibbonCommandContext context, ZuulProperties zuulProperties) {
        this(commandKey, client, context, zuulProperties, null);
    }

    public AbstractRibbonCommand(String commandKey, LBC client,
                                 RibbonCommandContext context, ZuulProperties zuulProperties,
                                 FallbackProvider fallbackProvider) {
        this(commandKey, client, context, zuulProperties, fallbackProvider, null);
    }

    public AbstractRibbonCommand(String commandKey, LBC client,
                                 RibbonCommandContext context, ZuulProperties zuulProperties,
                                 FallbackProvider fallbackProvider, IClientConfig config) {
        this(getSetter(commandKey, zuulProperties, config), client, context, fallbackProvider, config);
    }

    protected AbstractRibbonCommand(Setter setter, LBC client,
                                    RibbonCommandContext context,
                                    FallbackProvider fallbackProvider, IClientConfig config) {
        super(setter);
        this.client = client;
        this.context = context;
        this.zuulFallbackProvider = fallbackProvider;
        this.config = config;
    }

    protected static HystrixCommandProperties.Setter createSetter(IClientConfig config, String commandKey, ZuulProperties zuulProperties) {
        int hystrixTimeout = getHystrixTimeout(config, commandKey);
        return HystrixCommandProperties.Setter().withExecutionIsolationStrategy(
                zuulProperties.getRibbonIsolationStrategy()).withExecutionTimeoutInMilliseconds(hystrixTimeout);
    }

    protected static int getHystrixTimeout(IClientConfig config, String commandKey) {
        int ribbonTimeout = getRibbonTimeout(config, commandKey);
        DynamicPropertyFactory dynamicPropertyFactory = DynamicPropertyFactory.getInstance();
        int defaultHystrixTimeout = dynamicPropertyFactory.getIntProperty("hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds",
                0).get();
        int commandHystrixTimeout = dynamicPropertyFactory.getIntProperty("hystrix.command." + commandKey + ".execution.isolation.thread.timeoutInMilliseconds",
                0).get();
        int hystrixTimeout;
        if(commandHystrixTimeout > 0) {
            hystrixTimeout = commandHystrixTimeout;
        }
        else if(defaultHystrixTimeout > 0) {
            hystrixTimeout = defaultHystrixTimeout;
        } else {
            hystrixTimeout = ribbonTimeout;
        }
        if(hystrixTimeout < ribbonTimeout) {
            LOGGER.warn("The Hystrix timeout of " + hystrixTimeout + "ms for the command " + commandKey +
                    " is set lower than the combination of the Ribbon read and connect timeout, " + ribbonTimeout + "ms.");
        }
        return hystrixTimeout;
    }

    protected static int getRibbonTimeout(IClientConfig config, String commandKey) {
        int ribbonTimeout;
        if (config == null) {
            ribbonTimeout = RibbonClientConfiguration.DEFAULT_READ_TIMEOUT + RibbonClientConfiguration.DEFAULT_CONNECT_TIMEOUT;
        } else {
            int ribbonReadTimeout = getTimeout(config, commandKey, "ReadTimeout",
                    IClientConfigKey.Keys.ReadTimeout, RibbonClientConfiguration.DEFAULT_READ_TIMEOUT);
            int ribbonConnectTimeout = getTimeout(config, commandKey, "ConnectTimeout",
                    IClientConfigKey.Keys.ConnectTimeout, RibbonClientConfiguration.DEFAULT_CONNECT_TIMEOUT);
            int maxAutoRetries = getTimeout(config, commandKey, "MaxAutoRetries",
                    IClientConfigKey.Keys.MaxAutoRetries, DefaultClientConfigImpl.DEFAULT_MAX_AUTO_RETRIES);
            int maxAutoRetriesNextServer = getTimeout(config, commandKey, "MaxAutoRetriesNextServer",
                    IClientConfigKey.Keys.MaxAutoRetriesNextServer, DefaultClientConfigImpl.DEFAULT_MAX_AUTO_RETRIES_NEXT_SERVER);
            ribbonTimeout = (ribbonReadTimeout + ribbonConnectTimeout) * (maxAutoRetries + 1) * (maxAutoRetriesNextServer + 1);
        }
        return ribbonTimeout;
    }

    private static int getTimeout(IClientConfig config, String commandKey, String property, IClientConfigKey<Integer> configKey, int defaultValue) {
        DynamicPropertyFactory dynamicPropertyFactory = DynamicPropertyFactory.getInstance();
        return dynamicPropertyFactory.getIntProperty(commandKey + "." + config.getNameSpace() + "." + property, config.get(configKey, defaultValue)).get();
    }

    @Deprecated
    protected static Setter getSetter(final String commandKey, ZuulProperties zuulProperties) {
        return getSetter(commandKey, zuulProperties, null);
    }

    protected static Setter getSetter(final String commandKey,
                                      ZuulProperties zuulProperties, IClientConfig config) {

        // @formatter:off
        Setter commandSetter = Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RibbonCommand"))
                .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey));
        final HystrixCommandProperties.Setter setter = createSetter(config, commandKey, zuulProperties);
        if (zuulProperties.getRibbonIsolationStrategy() == HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE){
            final String name = ZuulConstants.ZUUL_EUREKA + commandKey + ".semaphore.maxSemaphores";
            // we want to default to semaphore-isolation since this wraps
            // 2 others commands that are already thread isolated
            final DynamicIntProperty value = DynamicPropertyFactory.getInstance()
                    .getIntProperty(name, zuulProperties.getSemaphore().getMaxSemaphores());
            setter.withExecutionIsolationSemaphoreMaxConcurrentRequests(value.get());
        } else if (zuulProperties.getThreadPool().isUseSeparateThreadPools()) {
            final String threadPoolKey = zuulProperties.getThreadPool().getThreadPoolKeyPrefix() + commandKey;
            commandSetter.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(threadPoolKey));
        }

        return commandSetter.andCommandPropertiesDefaults(setter);
        // @formatter:on
    }

    //转发的具体执行
    @Override
    protected ClientHttpResponse run() throws Exception {
        final RequestContext context = RequestContext.getCurrentContext();

        RQ request = createRequest();
        RS response;

        boolean retryableClient = this.client instanceof AbstractLoadBalancingClient
                && ((AbstractLoadBalancingClient)this.client).isClientRetryable((ContextAwareRequest)request);

        if (retryableClient) {
            response = this.client.execute(request, config);
        } else {
            //带负载均衡的转发执行
            response = this.client.executeWithLoadBalancer(request, config);
        }
        context.set("ribbonResponse", response);

        // Explicitly close the HttpResponse if the Hystrix command timed out to
        // release the underlying HTTP connection held by the response.
        //
        if (this.isResponseTimedOut()) {
            if (response != null) {
                response.close();
            }
        }

        return new RibbonHttpResponse(response);
    }

    @Override
    protected ClientHttpResponse getFallback() {
        if(zuulFallbackProvider != null) {
            return getFallbackResponse();
        }
        return super.getFallback();
    }

    protected ClientHttpResponse getFallbackResponse() {
        Throwable cause = getFailedExecutionException();
        cause = cause == null ? getExecutionException() : cause;
        return zuulFallbackProvider.fallbackResponse(context.getServiceId(), cause);
    }

    public LBC getClient() {
        return client;
    }

    public RibbonCommandContext getContext() {
        return context;
    }

    protected abstract RQ createRequest() throws Exception;
}
