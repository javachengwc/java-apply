package com.spring.pseudocode.web.http;

import com.spring.pseudocode.core.util.MultiValueMap;

public class HttpEntity<T> {
    public static final HttpEntity<?> EMPTY = new HttpEntity();
    private final HttpHeaders headers;
    private final T body;

    protected HttpEntity() {
        this(null, null);
    }

    public HttpEntity(T body) {
        this(body, null);
    }

    public HttpEntity(MultiValueMap<String, String> headers) {
        this(null, headers);
    }

    public HttpEntity(T body, MultiValueMap<String, String> headers) {
        this.body = body;
        HttpHeaders tempHeaders = new HttpHeaders();
        if (headers != null) {
            tempHeaders.putAll(headers);
        }
        this.headers = HttpHeaders.readOnlyHttpHeaders(tempHeaders);
    }

    public HttpHeaders getHeaders() {
        return this.headers;
    }

    public T getBody() {
        return this.body;
    }

    public boolean hasBody() {
        return this.body != null;
    }
}
