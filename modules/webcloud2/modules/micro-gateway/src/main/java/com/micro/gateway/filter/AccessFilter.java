package com.micro.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;

public class AccessFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "type";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        System.out.println(" AccessFilter run start ......");
        return null;
    }
}
