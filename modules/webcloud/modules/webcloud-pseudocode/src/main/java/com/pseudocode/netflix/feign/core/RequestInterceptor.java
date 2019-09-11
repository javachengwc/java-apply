package com.pseudocode.netflix.feign.core;

public interface RequestInterceptor {

    void apply(RequestTemplate template);
}
