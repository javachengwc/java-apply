package com.cloud.consumer.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class CommandFallback extends HystrixCommand<String> {

    private Boolean throwException;

    public CommandFallback(Boolean throwException) {
        super(HystrixCommandGroupKey.Factory.asKey("CommandFallback"));
        this.throwException=throwException;
    }

    @Override
    protected String run() {
        if(throwException) {
            throw new RuntimeException("CommandFallback run exception");
        }
        return "success";
    }

    @Override
    protected String getFallback() {
        return "fallback";
    }
}