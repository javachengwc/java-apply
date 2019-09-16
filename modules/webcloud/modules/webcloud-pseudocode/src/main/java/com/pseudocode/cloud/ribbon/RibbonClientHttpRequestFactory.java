package com.pseudocode.cloud.ribbon;


import java.io.IOException;
import java.net.URI;

import com.pseudocode.netflix.ribbon.core.client.config.IClientConfig;
import com.pseudocode.netflix.ribbon.httpclient.http.HttpRequest;
import com.pseudocode.netflix.ribbon.httpclient.niws.RestClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;


public class RibbonClientHttpRequestFactory implements ClientHttpRequestFactory {

    private final SpringClientFactory clientFactory;

    public RibbonClientHttpRequestFactory(SpringClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    @SuppressWarnings("deprecation")
    public ClientHttpRequest createRequest(URI originalUri, HttpMethod httpMethod)
            throws IOException {
        String serviceId = originalUri.getHost();
        if (serviceId == null) {
            throw new IOException("Invalid hostname in the URI [" + originalUri.toASCIIString() + "]");
        }
        IClientConfig clientConfig = this.clientFactory.getClientConfig(serviceId);
        RestClient client = this.clientFactory.getClient(serviceId, RestClient.class);
        HttpRequest.Verb verb = HttpRequest.Verb.valueOf(httpMethod.name());
        return new RibbonHttpRequest(originalUri, verb, client, clientConfig);
    }

}
