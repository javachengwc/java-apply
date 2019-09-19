package com.pseudocode.cloud.zuul.filters.route.apache;


import com.pseudocode.cloud.ribbon.SpringClientFactory;
import com.pseudocode.cloud.ribbon.apache.RibbonLoadBalancingHttpClient;
import com.pseudocode.cloud.ribbon.support.RibbonCommandContext;
import com.pseudocode.cloud.zuul.filters.ZuulProperties;

import java.util.Collections;
import java.util.Set;


public class HttpClientRibbonCommandFactory extends AbstractRibbonCommandFactory {

    private final SpringClientFactory clientFactory;

    private final ZuulProperties zuulProperties;

    public HttpClientRibbonCommandFactory(SpringClientFactory clientFactory, ZuulProperties zuulProperties) {
        this(clientFactory, zuulProperties, Collections.<FallbackProvider>emptySet());
    }

    public HttpClientRibbonCommandFactory(SpringClientFactory clientFactory, ZuulProperties zuulProperties,
                                          Set<FallbackProvider> fallbackProviders) {
        super(fallbackProviders);
        this.clientFactory = clientFactory;
        this.zuulProperties = zuulProperties;
    }

    @Override
    public HttpClientRibbonCommand create(final RibbonCommandContext context) {
        //zuul调用失败后的降级方法
        FallbackProvider zuulFallbackProvider = getFallbackProvider(context.getServiceId());
        final String serviceId = context.getServiceId();

        //创建处理请求转发类，该类会用Apache的Http client进行请求的转发
        final RibbonLoadBalancingHttpClient client = this.clientFactory.getClient(serviceId, RibbonLoadBalancingHttpClient.class);
        client.setLoadBalancer(this.clientFactory.getLoadBalancer(serviceId));

        return new HttpClientRibbonCommand(serviceId, client, context, zuulProperties, zuulFallbackProvider,
                clientFactory.getClientConfig(serviceId));
    }

}
