package com.pseudocode.netflix.zuul;

public interface IZuulFilter {

    boolean shouldFilter();

    Object run();

}
