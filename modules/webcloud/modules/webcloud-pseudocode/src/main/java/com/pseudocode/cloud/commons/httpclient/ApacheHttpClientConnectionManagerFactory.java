package com.pseudocode.cloud.commons.httpclient;


import java.util.concurrent.TimeUnit;

import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;

public interface ApacheHttpClientConnectionManagerFactory {

    public static final String HTTP_SCHEME = "http";

    public static final String HTTPS_SCHEME = "https";

    public HttpClientConnectionManager newConnectionManager(boolean disableSslValidation,
                                                            int maxTotalConnections, int maxConnectionsPerRoute, long timeToLive,
                                                            TimeUnit timeUnit, RegistryBuilder registryBuilder);
}
