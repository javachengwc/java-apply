package com.pseudocode.cloud.openfeign.core.ribbon;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.pseudocode.cloud.ribbon.RibbonProperties;
import com.pseudocode.cloud.ribbon.ServerIntrospector;
import com.pseudocode.netflix.feign.core.Client;
import com.pseudocode.netflix.feign.core.Request;
import com.pseudocode.netflix.feign.core.Response;
import com.pseudocode.netflix.ribbon.core.client.*;
import com.pseudocode.netflix.ribbon.core.client.config.IClientConfig;
import com.pseudocode.netflix.ribbon.loadbalancer.ILoadBalancer;
import com.pseudocode.netflix.ribbon.loadbalancer.client.AbstractLoadBalancerAwareClient;
import com.pseudocode.netflix.ribbon.loadbalancer.server.Server;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;

public class FeignLoadBalancer extends
        AbstractLoadBalancerAwareClient<FeignLoadBalancer.RibbonRequest, FeignLoadBalancer.RibbonResponse> {

    private final RibbonProperties ribbon;
    protected int connectTimeout;
    protected int readTimeout;
    protected IClientConfig clientConfig;
    protected ServerIntrospector serverIntrospector;

    public FeignLoadBalancer(ILoadBalancer lb, IClientConfig clientConfig,
                             ServerIntrospector serverIntrospector) {
        super(lb, clientConfig);
        this.setRetryHandler(RetryHandler.DEFAULT);
        this.clientConfig = clientConfig;
        this.ribbon = RibbonProperties.from(clientConfig);
        RibbonProperties ribbon = this.ribbon;
        this.connectTimeout = ribbon.getConnectTimeout();
        this.readTimeout = ribbon.getReadTimeout();
        this.serverIntrospector = serverIntrospector;
    }

    @Override
    public RibbonResponse execute(RibbonRequest request, IClientConfig configOverride) throws IOException {
        Request.Options options;
        if (configOverride != null) {
            RibbonProperties override = RibbonProperties.from(configOverride);
            options = new Request.Options(
                    override.connectTimeout(this.connectTimeout),
                    override.readTimeout(this.readTimeout));
        }
        else {
            options = new Request.Options(this.connectTimeout, this.readTimeout);
        }
        Response response = request.client().execute(request.toRequest(), options);
        return new RibbonResponse(request.getUri(), response);
    }

    @Override
    public RequestSpecificRetryHandler getRequestSpecificRetryHandler(
            RibbonRequest request, IClientConfig requestConfig) {
        if (this.ribbon.isOkToRetryOnAllOperations()) {
            return new RequestSpecificRetryHandler(true, true, this.getRetryHandler(),
                    requestConfig);
        }
        if (!request.toRequest().method().equals("GET")) {
            return new RequestSpecificRetryHandler(true, false, this.getRetryHandler(),
                    requestConfig);
        }
        else {
            return new RequestSpecificRetryHandler(true, true, this.getRetryHandler(),
                    requestConfig);
        }
    }

    @Override
    public URI reconstructURIWithServer(Server server, URI original) {
        URI uri = updateToSecureConnectionIfNeeded(original, this.clientConfig, this.serverIntrospector, server);
        return super.reconstructURIWithServer(server, uri);
    }

    protected static class RibbonRequest extends ClientRequest implements Cloneable {

        private final Request request;
        private final Client client;

        RibbonRequest(Client client, Request request, URI uri) {
            this.client = client;
            setUri(uri);
            this.request = toRequest(request);
        }

        private Request toRequest(Request request) {
            Map<String, Collection<String>> headers = new LinkedHashMap<>(
                    request.headers());
            return Request.create(request.method(),getUri().toASCIIString(),headers,request.body(),request.charset());
        }

        Request toRequest() {
            return toRequest(this.request);
        }

        Client client() {
            return this.client;
        }

        HttpRequest toHttpRequest() {
            return new HttpRequest() {
                @Override
                public HttpMethod getMethod() {
                    return HttpMethod.resolve(RibbonRequest.this.toRequest().method());
                }

                @Override
                public String getMethodValue() {
                    return getMethod().name();
                }

                @Override
                public URI getURI() {
                    return RibbonRequest.this.getUri();
                }

                @Override
                public HttpHeaders getHeaders() {
                    Map<String, List<String>> headers = new HashMap<>();
                    Map<String, Collection<String>> feignHeaders = RibbonRequest.this.toRequest().headers();
                    for(String key : feignHeaders.keySet()) {
                        headers.put(key, new ArrayList<String>(feignHeaders.get(key)));
                    }
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.putAll(headers);
                    return httpHeaders;

                }
            };
        }


        @Override
        public Object clone() {
            return new RibbonRequest(this.client, this.request, getUri());
        }
    }

    protected static class RibbonResponse implements IResponse {

        private final URI uri;
        private final Response response;

        RibbonResponse(URI uri, Response response) {
            this.uri = uri;
            this.response = response;
        }

        @Override
        public Object getPayload() throws ClientException {
            return this.response.body();
        }

        @Override
        public boolean hasPayload() {
            return this.response.body() != null;
        }

        @Override
        public boolean isSuccess() {
            return this.response.status() == 200;
        }

        @Override
        public URI getRequestedURI() {
            return this.uri;
        }

        @Override
        public Map<String, Collection<String>> getHeaders() {
            return this.response.headers();
        }

        Response toResponse() {
            return this.response;
        }

        @Override
        public void close() throws IOException {
            if (this.response != null && this.response.body() != null) {
                this.response.body().close();
            }
        }

    }

}
