package com.spring.pseudocode.web.http.client;

import java.io.IOException;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;

public interface ClientHttpRequest extends HttpRequest, HttpOutputMessage {

    ClientHttpResponse execute() throws IOException;

}
