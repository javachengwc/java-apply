package com.spring.pseudocode.web.http.client;


import java.net.URI;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.AbstractClientHttpRequestFactoryWrapper;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;

public class InterceptingClientHttpRequestFactory extends AbstractClientHttpRequestFactoryWrapper {

    private final List<ClientHttpRequestInterceptor> interceptors;

    public InterceptingClientHttpRequestFactory(ClientHttpRequestFactory requestFactory,
                                                List<ClientHttpRequestInterceptor> interceptors) {

        super(requestFactory);
        this.interceptors = (interceptors != null ? interceptors : Collections.<ClientHttpRequestInterceptor>emptyList());
    }

    @Override
    protected ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod, ClientHttpRequestFactory requestFactory) {
        return new InterceptingClientHttpRequest(requestFactory, this.interceptors, uri, httpMethod);
    }

}
