package com.spring.pseudocode.web.http.client;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;

public interface ClientHttpRequestExecution {

    ClientHttpResponse execute(HttpRequest request, byte[] body) throws IOException;

}