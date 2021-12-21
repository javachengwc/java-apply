package com.spring.pseudocode.web.http.client;


import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
//import org.springframework.http.client.AbstractBufferingClientHttpRequest;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

public class InterceptingClientHttpRequest
        //extends AbstractBufferingClientHttpRequest
        {

    private final ClientHttpRequestFactory requestFactory;

    private final List<ClientHttpRequestInterceptor> interceptors;

    private HttpMethod method;

    private URI uri;


    protected InterceptingClientHttpRequest(ClientHttpRequestFactory requestFactory,
                                            List<ClientHttpRequestInterceptor> interceptors, URI uri, HttpMethod method) {

        this.requestFactory = requestFactory;
        this.interceptors = interceptors;
        this.method = method;
        this.uri = uri;
    }


    //@Override
    public HttpMethod getMethod() {
        return this.method;
    }

    //@Override
    public URI getURI() {
        return this.uri;
    }

    //@Override
    protected final ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutput) throws IOException {
        InterceptingRequestExecution requestExecution = new InterceptingRequestExecution();
        //return requestExecution.execute(this, bufferedOutput);
        return null;
    }


    private class InterceptingRequestExecution implements ClientHttpRequestExecution {

        private final Iterator<ClientHttpRequestInterceptor> iterator;

        public InterceptingRequestExecution() {
            this.iterator = interceptors.iterator();
        }

        @Override
        public ClientHttpResponse execute(HttpRequest request, byte[] body) throws IOException {
            if (this.iterator.hasNext()) {
                ClientHttpRequestInterceptor nextInterceptor = this.iterator.next();
                return nextInterceptor.intercept(request, body, this);
            }
            else {
                ClientHttpRequest delegate = requestFactory.createRequest(request.getURI(), request.getMethod());
                delegate.getHeaders().putAll(request.getHeaders());
                if (body.length > 0) {
                    StreamUtils.copy(body, delegate.getBody());
                }
                return delegate.execute();
            }
        }
    }

}
