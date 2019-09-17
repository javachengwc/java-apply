package com.pseudocode.cloud.openfeign.core.ribbon;


import java.io.IOException;
import java.net.URI;

import com.pseudocode.cloud.ribbon.SpringClientFactory;
import com.pseudocode.netflix.feign.core.Client;
import com.pseudocode.netflix.feign.core.Request;
import com.pseudocode.netflix.feign.core.Response;
import com.pseudocode.netflix.ribbon.core.client.ClientException;
import com.pseudocode.netflix.ribbon.core.client.config.CommonClientConfigKey;
import com.pseudocode.netflix.ribbon.core.client.config.DefaultClientConfigImpl;
import com.pseudocode.netflix.ribbon.core.client.config.IClientConfig;

public class LoadBalancerFeignClient implements Client {

    static final Request.Options DEFAULT_OPTIONS = new Request.Options();

    private final Client delegate;
    private CachingSpringLoadBalancerFactory lbClientFactory;
    private SpringClientFactory clientFactory;

    public LoadBalancerFeignClient(Client delegate,
                                   CachingSpringLoadBalancerFactory lbClientFactory,
                                   SpringClientFactory clientFactory) {
        this.delegate = delegate;
        this.lbClientFactory = lbClientFactory;
        this.clientFactory = clientFactory;
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        try {
            //得到完整的请求地址 http://xxxx/xx?xx=xx
            URI asUri = URI.create(request.url());
            String clientName = asUri.getHost();
            URI uriWithoutHost = cleanUrl(request.url(), clientName);
            //构建feign负载均衡的请求对象
            FeignLoadBalancer.RibbonRequest ribbonRequest = new FeignLoadBalancer.RibbonRequest(
                    this.delegate, request, uriWithoutHost);

            //客户端配置
            IClientConfig requestConfig = getClientConfig(options, clientName);

            //发送请求
            return lbClient(clientName).executeWithLoadBalancer(ribbonRequest, requestConfig).toResponse();
        } catch (ClientException e) {
            IOException io = findIOException(e);
            if (io != null) {
                throw io;
            }
            throw new RuntimeException(e);
        }
    }

    IClientConfig getClientConfig(Request.Options options, String clientName) {
        IClientConfig requestConfig;
        if (options == DEFAULT_OPTIONS) {
            requestConfig = this.clientFactory.getClientConfig(clientName);
        } else {
            requestConfig = new FeignOptionsClientConfig(options);
        }
        return requestConfig;
    }

    protected IOException findIOException(Throwable t) {
        if (t == null) {
            return null;
        }
        if (t instanceof IOException) {
            return (IOException) t;
        }
        return findIOException(t.getCause());
    }

    public Client getDelegate() {
        return this.delegate;
    }

    static URI cleanUrl(String originalUrl, String host) {
        return URI.create(originalUrl.replaceFirst(host, ""));
    }

    private FeignLoadBalancer lbClient(String clientName) {
        return this.lbClientFactory.create(clientName);
    }

    static class FeignOptionsClientConfig extends DefaultClientConfigImpl {

        public FeignOptionsClientConfig(Request.Options options) {
            setProperty(CommonClientConfigKey.ConnectTimeout, options.connectTimeoutMillis());
            setProperty(CommonClientConfigKey.ReadTimeout, options.readTimeoutMillis());
        }

        @Override
        public void loadProperties(String clientName) {

        }

        @Override
        public void loadDefaultValues() {

        }

    }
}

