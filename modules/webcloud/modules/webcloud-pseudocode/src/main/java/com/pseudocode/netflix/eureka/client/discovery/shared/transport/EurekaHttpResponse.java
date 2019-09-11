package com.pseudocode.netflix.eureka.client.discovery.shared.transport;


import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EurekaHttpResponse<T> {
    private final int statusCode;
    private final T entity;
    private final Map<String, String> headers;
    private final URI location;

    protected EurekaHttpResponse(int statusCode, T entity) {
        this.statusCode = statusCode;
        this.entity = entity;
        this.headers = null;
        this.location = null;
    }

    private EurekaHttpResponse(EurekaHttpResponseBuilder<T> builder) {
        this.statusCode = builder.statusCode;
        this.entity = builder.entity;
        this.headers = builder.headers;

        if (headers != null) {
            String locationValue = headers.get(HttpHeaders.LOCATION);
            try {
                this.location = locationValue == null ? null : new URI(locationValue);
            } catch (URISyntaxException e) {
                //throw new TransportException("Invalid Location header value in response; cannot complete the request (location=" + locationValue + ')', e);
                throw new RuntimeException("Invalid Location header value in response; cannot complete the request (location=" + locationValue + ')', e);
            }
        } else {
            this.location = null;
        }
    }

    public int getStatusCode() {
        return statusCode;
    }

    public URI getLocation() {
        return location;
    }

    public Map<String, String> getHeaders() {
        return headers == null ? Collections.<String, String>emptyMap() : headers;
    }

    public T getEntity() {
        return entity;
    }

    public static EurekaHttpResponse<Void> status(int status) {
        return new EurekaHttpResponse<>(status, null);
    }

    public static EurekaHttpResponseBuilder<Void> anEurekaHttpResponse(int statusCode) {
        return new EurekaHttpResponseBuilder<>(statusCode);
    }

    public static <T> EurekaHttpResponseBuilder<T> anEurekaHttpResponse(int statusCode, Class<T> entityType) {
        return new EurekaHttpResponseBuilder<T>(statusCode);
    }

    public static <T> EurekaHttpResponseBuilder<T> anEurekaHttpResponse(int statusCode, T entity) {
        return new EurekaHttpResponseBuilder<T>(statusCode).entity(entity);
    }

    public static class EurekaHttpResponseBuilder<T> {

        private final int statusCode;
        private T entity;
        private Map<String, String> headers;

        private EurekaHttpResponseBuilder(int statusCode) {
            this.statusCode = statusCode;
        }

        public EurekaHttpResponseBuilder<T> entity(T entity) {
            this.entity = entity;
            return this;
        }

        public EurekaHttpResponseBuilder<T> entity(T entity, MediaType contentType) {
            return entity(entity).type(contentType);
        }

        public EurekaHttpResponseBuilder<T> type(MediaType contentType) {
            headers(HttpHeaders.CONTENT_TYPE, contentType.toString());
            return this;
        }

        public EurekaHttpResponseBuilder<T> headers(String name, Object value) {
            if (headers == null) {
                headers = new HashMap<>();
            }
            headers.put(name, value.toString());
            return this;
        }

        public EurekaHttpResponseBuilder<T> headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public EurekaHttpResponse<T> build() {
            return new EurekaHttpResponse<T>(this);
        }
    }
}
