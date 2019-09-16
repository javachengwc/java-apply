package com.pseudocode.cloud.ribbon;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.pseudocode.netflix.ribbon.httpclient.http.HttpResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.AbstractClientHttpResponse;

public class RibbonHttpResponse extends AbstractClientHttpResponse {

    private HttpResponse response;
    private HttpHeaders httpHeaders;

    public RibbonHttpResponse(HttpResponse response) {
        this.response = response;
        this.httpHeaders = new HttpHeaders();
        List<Map.Entry<String, String>> headers = response.getHttpHeaders()
                .getAllHeaders();
        for (Map.Entry<String, String> header : headers) {
            this.httpHeaders.add(header.getKey(), header.getValue());
        }
    }

    @Override
    public InputStream getBody() throws IOException {
        return response.getInputStream();
    }

    @Override
    public HttpHeaders getHeaders() {
        return this.httpHeaders;
    }

    @Override
    public int getRawStatusCode() throws IOException {
        return response.getStatus();
    }

    @Override
    public String getStatusText() throws IOException {
        return HttpStatus.valueOf(response.getStatus()).name();
    }

    @Override
    public void close() {
        response.close();
    }

}
