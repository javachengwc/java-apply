package com.spring.pseudocode.web.http.client;


import java.io.IOException;
import java.net.URI;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;

public interface ClientHttpRequestFactory {

    ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException;

}
