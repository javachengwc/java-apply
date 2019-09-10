package com.pseudocode.cloud.commons.httpclient;

import org.apache.http.impl.client.HttpClientBuilder;

public class DefaultApacheHttpClientFactory implements ApacheHttpClientFactory {

    private HttpClientBuilder builder;

    public DefaultApacheHttpClientFactory(HttpClientBuilder builder) {
        this.builder = builder;
    }

    @Override
    public HttpClientBuilder createBuilder() {
        //默认不压缩内容，不进行cookie管理
        return this.builder.disableContentCompression().disableCookieManagement().useSystemProperties();
    }
}
