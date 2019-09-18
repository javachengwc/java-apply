package com.pseudocode.netflix.zuul;

public interface IZuulFilter {

    //是否过滤
    boolean shouldFilter();

    //具体执行
    Object run();

}
