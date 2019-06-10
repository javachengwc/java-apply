package com.pseudocode.cloud.zuul;

public interface IZuulFilter {

    boolean shouldFilter();

    Object run();

}
