package com.pseudocode.cloud.ribbon.support;


import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StreamUtils;

public class RibbonCommandContext {
    private final String serviceId;
    private final String method;
    private final String uri;
    private final Boolean retryable;
    private final MultiValueMap<String, String> headers;
    private final MultiValueMap<String, String> params;
    private final List<RibbonRequestCustomizer> requestCustomizers;
    private InputStream requestEntity;
    private Long contentLength;
    private Object loadBalancerKey;

    @Deprecated
    public RibbonCommandContext(String serviceId, String method,
                                String uri, Boolean retryable, MultiValueMap<String, String> headers,
                                MultiValueMap<String, String> params, InputStream requestEntity) {
        this(serviceId, method, uri, retryable, headers, params, requestEntity,
                new ArrayList<RibbonRequestCustomizer>(), null, null);
    }

    public RibbonCommandContext(String serviceId, String method, String uri,
                                Boolean retryable, MultiValueMap<String, String> headers,
                                MultiValueMap<String, String> params, InputStream requestEntity,
                                List<RibbonRequestCustomizer> requestCustomizers) {
        this(serviceId, method, uri, retryable, headers, params, requestEntity,
                requestCustomizers, null, null);
    }

    public RibbonCommandContext(String serviceId, String method, String uri,
                                Boolean retryable, MultiValueMap<String, String> headers,
                                MultiValueMap<String, String> params, InputStream requestEntity,
                                List<RibbonRequestCustomizer> requestCustomizers, Long contentLength) {
        this(serviceId, method, uri, retryable, headers, params, requestEntity,
                requestCustomizers, contentLength, null);
    }

    public RibbonCommandContext(String serviceId, String method, String uri,
                                Boolean retryable, MultiValueMap<String, String> headers,
                                MultiValueMap<String, String> params, InputStream requestEntity,
                                List<RibbonRequestCustomizer> requestCustomizers, Long contentLength,
                                Object loadBalancerKey) {
        Assert.notNull(serviceId, "serviceId may not be null");
        Assert.notNull(method, "method may not be null");
        Assert.notNull(uri, "uri may not be null");
        Assert.notNull(headers, "headers may not be null");
        Assert.notNull(params, "params may not be null");
        Assert.notNull(requestCustomizers, "requestCustomizers may not be null");
        this.serviceId = serviceId;
        this.method = method;
        this.uri = uri;
        this.retryable = retryable;
        this.headers = headers;
        this.params = params;
        this.requestEntity = requestEntity;
        this.requestCustomizers = requestCustomizers;
        this.contentLength = contentLength;
        this.loadBalancerKey = loadBalancerKey;
    }

    public URI uri() {
        try {
            return new URI(this.uri);
        } catch (URISyntaxException e) {
            ReflectionUtils.rethrowRuntimeException(e);
        }
        return null;
    }

    @Deprecated
    public String getVerb() {
        return this.method;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public Boolean getRetryable() {
        return retryable;
    }

    public MultiValueMap<String, String> getHeaders() {
        return headers;
    }

    public MultiValueMap<String, String> getParams() {
        return params;
    }

    public InputStream getRequestEntity() {
        if (requestEntity == null) {
            return null;
        }

        try {
            if (!(requestEntity instanceof ResettableServletInputStreamWrapper)) {
                requestEntity = new ResettableServletInputStreamWrapper(StreamUtils.copyToByteArray(requestEntity));
            }
            requestEntity.reset();
        } finally {
            return requestEntity;
        }
    }

    public List<RibbonRequestCustomizer> getRequestCustomizers() {
        return requestCustomizers;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }

    public Object getLoadBalancerKey() {
        return loadBalancerKey;
    }

    public void setLoadBalancerKey(Object loadBalancerKey) {
        this.loadBalancerKey = loadBalancerKey;
    }

}
