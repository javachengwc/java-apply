package com.pseudocode.cloud.zuul.constants;

public class ZuulHeaders {
    public static final String TRANSFER_ENCODING = "transfer-encoding";
    public static final String CHUNKED = "chunked";
    public static final String ORIGIN = "Origin";
    public static final String CONTENT_ENCODING = "Content-Encoding";
    public static final String ACCEPT_ENCODING = "accept-encoding";
    public static final String X_ZUUL = "X-Zuul";
    public static final String X_ZUUL_INSTANCE = "X-Zuul-instance";
    public static final String CONNECTION = "Connection";
    public static final String KEEP_ALIVE = "keep-alive";
    public static final String X_ORIGINATING_URL = "X-Originating-URL";
    public static final String X_NETFLIX_ERROR_CAUSE = "X-Netflix-Error-Cause";
    public static final String X_FORWARDED_PROTO = "X-Forwarded-Proto";
    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    public static final String X_NETFLIX_CLIENT_HOST = "X-Netflix.client-host";
    public static final String HOST = "Host";
    public static final String X_NETFLIX_CLIENT_PROTO = "X-Netflix.client-proto";
    public static final String X_ZUUL_SURGICAL_FILTER = "X-Zuul-Surgical-Filter";
    public static final String X_ZUUL_FILTER_EXECUTION_STATUS = "X-Zuul-Filter-Executions";

    private ZuulHeaders() {
        throw new AssertionError("Must not instantiate constant utility class");
    }
}
