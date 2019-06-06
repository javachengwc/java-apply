package com.cloud.pseudocode.common.client.loadbalancer;

import org.springframework.http.HttpHeaders;
import org.springframework.http.client.AbstractClientHttpResponse;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ClientHttpResponseStatusCodeException extends RetryableStatusCodeException {

    private ClientHttpResponseWrapper response;

    public ClientHttpResponseStatusCodeException(String serviceId, ClientHttpResponse response, byte[] body) throws IOException {
        super(serviceId, response.getRawStatusCode(), response, null);
        this.response = new ClientHttpResponseWrapper(response, body);
    }

    @Override
    public ClientHttpResponse getResponse() {
        return response;
    }

    static class ClientHttpResponseWrapper extends AbstractClientHttpResponse {

        private ClientHttpResponse response;
        private byte[] body;

        public ClientHttpResponseWrapper(ClientHttpResponse response, byte[] body) {
            this.response = response;
            this.body = body;
        }

        @Override
        public int getRawStatusCode() throws IOException {
            return response.getRawStatusCode();
        }

        @Override
        public String getStatusText() throws IOException {
            return response.getStatusText();
        }

        @Override
        public void close() {
            response.close();
        }

        @Override
        public InputStream getBody() throws IOException {
            return new ByteArrayInputStream(body);
        }

        @Override
        public HttpHeaders getHeaders() {
            return response.getHeaders();
        }
    }
}
