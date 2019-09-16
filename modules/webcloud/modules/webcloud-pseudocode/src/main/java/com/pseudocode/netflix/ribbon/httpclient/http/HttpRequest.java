package com.pseudocode.netflix.ribbon.httpclient.http;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;


import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.pseudocode.netflix.ribbon.core.client.ClientRequest;
import com.pseudocode.netflix.ribbon.core.client.config.IClientConfig;

public class HttpRequest extends ClientRequest {

    public enum Verb {
        GET("GET"),
        PUT("PUT"),
        POST("POST"),
        DELETE("DELETE"),
        OPTIONS("OPTIONS"),
        HEAD("HEAD");

        private final String verb; // http method

        Verb(String verb) {
            this.verb = verb;
        }

        public String verb() {
            return verb;
        }
    }

    protected CaseInsensitiveMultiMap httpHeaders = new CaseInsensitiveMultiMap();
    protected Multimap<String, String> queryParams = ArrayListMultimap.create();
    private Object entity;
    protected Verb verb;

    HttpRequest() {
        this.verb = Verb.GET;
    }

    public static class Builder {

        private HttpRequest request = new HttpRequest();

        public Builder() {
        }

        public Builder(HttpRequest request) {
            this.request = request;
        }

        public Builder uri(URI uri) {
            request.setUri(uri);
            return this;
        }

        public Builder uri(String uri) {
            try {
                request.setUri(new URI(uri));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            return this;
        }

        public Builder header(String name, String value) {
            request.httpHeaders.addHeader(name, value);
            return this;
        }

        Builder queryParams(Multimap<String, String> queryParams) {
            request.queryParams = queryParams;
            return this;
        }

        @Deprecated
        public Builder overrideConfig(IClientConfig config) {
            request.setOverrideConfig(config);
            return this;
        }

        Builder headers(CaseInsensitiveMultiMap headers) {
            request.httpHeaders = headers;
            return this;
        }

        public Builder setRetriable(boolean retriable) {
            request.setRetriable(retriable);
            return this;
        }

        /**
         * @deprecated see {@link #queryParam(String, String)}
         */
        @Deprecated
        public Builder queryParams(String name, String value) {
            request.queryParams.put(name, value);
            return this;
        }

        public Builder queryParam(String name, String value) {
            request.queryParams.put(name, value);
            return this;
        }

        public Builder entity(Object entity) {
            request.entity = entity;
            return this;
        }


        public Builder verb(Verb verb) {
            request.verb = verb;
            return this;
        }

        public Builder loadBalancerKey(Object loadBalancerKey) {
            request.setLoadBalancerKey(loadBalancerKey);
            return this;
        }

        public HttpRequest build() {
            return request;
        }
    }

    public Map<String, Collection<String>> getQueryParams() {
        return queryParams.asMap();
    }

    public Verb getVerb() {
        return verb;
    }

    /**
     * Replaced by {@link #getHttpHeaders()}
     */
    @Deprecated
    public Map<String, Collection<String>>  getHeaders() {
        return httpHeaders.asMap();
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    public Object getEntity() {
        return entity;
    }

    @Override
    public boolean isRetriable() {
        if (this.verb == Verb.GET && isRetriable == null) {
            return true;
        }
        return super.isRetriable();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(HttpRequest toCopy) {
        return new Builder(toCopy);
    }

    @Override
    public HttpRequest replaceUri(URI newURI) {
        return (new Builder()).uri(newURI)
                .headers(this.httpHeaders)
                .overrideConfig(this.getOverrideConfig())
                .queryParams(this.queryParams)
                .setRetriable(this.isRetriable())
                .loadBalancerKey(this.getLoadBalancerKey())
                .verb(this.getVerb())
                .entity(this.entity)
                .build();
    }
}

