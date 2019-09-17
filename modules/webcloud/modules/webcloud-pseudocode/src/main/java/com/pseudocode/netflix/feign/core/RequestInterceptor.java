package com.pseudocode.netflix.feign.core;

//feign请求拦截器
public interface RequestInterceptor {

    void apply(RequestTemplate template);
}
