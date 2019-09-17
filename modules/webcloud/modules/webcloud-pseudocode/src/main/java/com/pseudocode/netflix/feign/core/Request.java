package com.pseudocode.netflix.feign.core;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;

public final class Request
{
    private final String method;
    private final String url;
    private final Map<String, Collection<String>> headers;
    private final byte[] body;
    private final Charset charset;

    public static Request create(String method, String url, Map<String, Collection<String>> headers, byte[] body, Charset charset)
    {
        return new Request(method, url, headers, body, charset);
    }

    Request(String method, String url, Map<String, Collection<String>> headers, byte[] body, Charset charset)
    {
        this.method = method;
        this.url = url;
        this.headers = headers;
        this.body = body;
        this.charset = charset;
    }

    public String method()
    {
        return this.method;
    }

    public String url()
    {
        return this.url;
    }

    public Map<String, Collection<String>> headers()
    {
        return this.headers;
    }

    public Charset charset()
    {
        return this.charset;
    }

    public byte[] body()
    {
        return this.body;
    }

    //超时时间，默认连接超时10s，读超时60s
    public static class Options {
        private final int connectTimeoutMillis;
        private final int readTimeoutMillis;

        public Options(int connectTimeoutMillis, int readTimeoutMillis) {
            this.connectTimeoutMillis = connectTimeoutMillis;
            this.readTimeoutMillis = readTimeoutMillis;
        }

        public Options() {
            this(10000, 60000);
        }

        public int connectTimeoutMillis()
        {
            return this.connectTimeoutMillis;
        }

        public int readTimeoutMillis()
        {
            return this.readTimeoutMillis;
        }
    }
}
