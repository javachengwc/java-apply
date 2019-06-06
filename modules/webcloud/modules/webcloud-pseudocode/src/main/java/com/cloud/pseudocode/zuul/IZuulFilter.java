package com.cloud.pseudocode.zuul;

public interface IZuulFilter {

    boolean shouldFilter();

    Object run();

}
