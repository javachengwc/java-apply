package com.pseudocode.cloud.commons.httpclient;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public interface ApacheHttpClientFactory {

    public HttpClientBuilder createBuilder();
}

