package com.pseudocode.cloud.ribbon.apache;


import java.net.URI;

import com.pseudocode.cloud.ribbon.RibbonProperties;
import com.pseudocode.cloud.ribbon.ServerIntrospector;
import com.pseudocode.cloud.ribbon.support.AbstractLoadBalancingClient;
import com.pseudocode.netflix.ribbon.core.client.RequestSpecificRetryHandler;
import com.pseudocode.netflix.ribbon.core.client.RetryHandler;
import com.pseudocode.netflix.ribbon.core.client.config.IClientConfig;
import com.pseudocode.netflix.ribbon.loadbalancer.server.Server;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.web.util.UriComponentsBuilder;

public class RibbonLoadBalancingHttpClient extends
        AbstractLoadBalancingClient<RibbonApacheHttpRequest, RibbonApacheHttpResponse, CloseableHttpClient> {

    public RibbonLoadBalancingHttpClient(IClientConfig config,
                                         ServerIntrospector serverIntrospector) {
        super(config, serverIntrospector);
    }

    public RibbonLoadBalancingHttpClient(CloseableHttpClient delegate,
                                         IClientConfig config, ServerIntrospector serverIntrospector) {
        super(delegate, config, serverIntrospector);
    }

    protected CloseableHttpClient createDelegate(IClientConfig config) {
        RibbonProperties ribbon = RibbonProperties.from(config);
        return HttpClientBuilder.create()
                // already defaults to 0 in builder, so resetting to 0 won't hurt
                .setMaxConnTotal(ribbon.maxTotalConnections(0))
                // already defaults to 0 in builder, so resetting to 0 won't hurt
                .setMaxConnPerRoute(ribbon.maxConnectionsPerHost(0))
                .disableCookieManagement().useSystemProperties() // for proxy
                .build();
    }

    @Override
    public RibbonApacheHttpResponse execute(RibbonApacheHttpRequest request,
                                            final IClientConfig configOverride) throws Exception {
        IClientConfig config = configOverride != null ? configOverride : this.config;
        RibbonProperties ribbon = RibbonProperties.from(config);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(ribbon.connectTimeout(this.connectTimeout))
                .setSocketTimeout(ribbon.readTimeout(this.readTimeout))
                .setRedirectsEnabled(ribbon.isFollowRedirects(this.followRedirects))
                .build();

        request = getSecureRequest(request, configOverride);
        final HttpUriRequest httpUriRequest = request.toRequest(requestConfig);
        final HttpResponse httpResponse = this.delegate.execute(httpUriRequest);
        return new RibbonApacheHttpResponse(httpResponse, httpUriRequest.getURI());
    }

    @Override
    public URI reconstructURIWithServer(Server server, URI original) {
        URI uri = updateToSecureConnectionIfNeeded(original, this.config, this.serverIntrospector,
                server);
        return super.reconstructURIWithServer(server, uri);
    }

    @Override
    public RequestSpecificRetryHandler getRequestSpecificRetryHandler(
            RibbonApacheHttpRequest request, IClientConfig requestConfig) {
        return new RequestSpecificRetryHandler(false, false, RetryHandler.DEFAULT,
                requestConfig);
    }

    protected RibbonApacheHttpRequest getSecureRequest(RibbonApacheHttpRequest request, IClientConfig configOverride) {
        if (isSecure(configOverride)) {
            final URI secureUri = UriComponentsBuilder.fromUri(request.getUri())
                    .scheme("https").build(true).toUri();
            return request.withNewUri(secureUri);
        }
        return request;
    }
}