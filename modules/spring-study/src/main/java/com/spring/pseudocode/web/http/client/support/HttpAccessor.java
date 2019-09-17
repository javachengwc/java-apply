package com.spring.pseudocode.web.http.client.support;

import java.io.IOException;
import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.Assert;

public abstract class HttpAccessor {

    protected final Log logger = LogFactory.getLog(getClass());

    private ClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

    public void setRequestFactory(ClientHttpRequestFactory requestFactory) {
        Assert.notNull(requestFactory, "'requestFactory' must not be null");
        this.requestFactory = requestFactory;
    }

    public ClientHttpRequestFactory getRequestFactory() {
        return this.requestFactory;
    }

    protected ClientHttpRequest createRequest(URI url, HttpMethod method) throws IOException {
        ClientHttpRequest request = getRequestFactory().createRequest(url, method);
        if (logger.isDebugEnabled()) {
            logger.debug("Created " + method.name() + " request for \"" + url + "\"");
        }
        return request;
    }

}
