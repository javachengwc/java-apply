package com.pseudocode.cloud.zuul.filters.route.apache;


import com.pseudocode.cloud.ribbon.apache.RibbonLoadBalancingHttpClient;
import com.pseudocode.cloud.ribbon.support.RibbonCommandContext;
import com.pseudocode.cloud.zuul.filters.ZuulProperties;
import com.pseudocode.netflix.ribbon.core.client.config.IClientConfig;

public class HttpClientRibbonCommand extends AbstractRibbonCommand<RibbonLoadBalancingHttpClient, RibbonApacheHttpRequest, RibbonApacheHttpResponse> {

    public HttpClientRibbonCommand(final String commandKey,
                                   final RibbonLoadBalancingHttpClient client,
                                   final RibbonCommandContext context,
                                   final ZuulProperties zuulProperties) {
        super(commandKey, client, context, zuulProperties);
    }

    public HttpClientRibbonCommand(final String commandKey,
                                   final RibbonLoadBalancingHttpClient client,
                                   final RibbonCommandContext context,
                                   final ZuulProperties zuulProperties,
                                   final FallbackProvider zuulFallbackProvider) {
        super(commandKey, client, context, zuulProperties, zuulFallbackProvider);
    }

    public HttpClientRibbonCommand(final String commandKey,
                                   final RibbonLoadBalancingHttpClient client,
                                   final RibbonCommandContext context,
                                   final ZuulProperties zuulProperties,
                                   final FallbackProvider zuulFallbackProvider,
                                   final IClientConfig config) {
        super(commandKey, client, context, zuulProperties, zuulFallbackProvider, config);
    }

    @Override
    protected RibbonApacheHttpRequest createRequest() throws Exception {
        return new RibbonApacheHttpRequest(this.context);
    }

}