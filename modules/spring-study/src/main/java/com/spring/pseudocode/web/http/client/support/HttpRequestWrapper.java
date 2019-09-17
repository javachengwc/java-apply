package com.spring.pseudocode.web.http.client.support;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;

import java.net.URI;

public class HttpRequestWrapper implements HttpRequest {

    private final HttpRequest request;

    public HttpRequestWrapper(HttpRequest request) {
        //Assert.notNull(request, "HttpRequest must not be null");
        this.request = request;
    }

    public HttpRequest getRequest() {
        return this.request;
    }

    @Override
    public HttpMethod getMethod() {
        return this.request.getMethod();
    }

    @Override
    public URI getURI() {
        return this.request.getURI();
    }

    @Override
    public HttpHeaders getHeaders() {
        return this.request.getHeaders();
    }

}

