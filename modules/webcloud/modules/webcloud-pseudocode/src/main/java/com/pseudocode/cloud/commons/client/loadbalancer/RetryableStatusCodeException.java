package com.pseudocode.cloud.commons.client.loadbalancer;

import java.io.IOException;
import java.net.URI;

public class RetryableStatusCodeException extends IOException {

    private static final String MESSAGE = "Service %s returned a status code of %d";
    private Object response;
    private URI uri;

    public RetryableStatusCodeException(String serviceId, int statusCode, Object response, URI uri) {
        super(String.format("Service %s returned a status code of %d", serviceId, statusCode));
        this.response = response;
        this.uri = uri;
    }

    public Object getResponse() {
        return this.response;
    }

    public URI getUri() {
        return this.uri;
    }
}
