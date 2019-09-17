package com.spring.pseudocode.web.http.client.support;


import java.util.ArrayList;
import java.util.List;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.http.client.support.HttpAccessor;
import org.springframework.util.CollectionUtils;

public abstract class InterceptingHttpAccessor extends HttpAccessor {

    private List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();

    public void setInterceptors(List<ClientHttpRequestInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public List<ClientHttpRequestInterceptor> getInterceptors() {
        return interceptors;
    }

    @Override
    public ClientHttpRequestFactory getRequestFactory() {
        ClientHttpRequestFactory delegate = super.getRequestFactory();
        if (!CollectionUtils.isEmpty(getInterceptors())) {
            return new InterceptingClientHttpRequestFactory(delegate, getInterceptors());
        }
        else {
            return delegate;
        }
    }

}
