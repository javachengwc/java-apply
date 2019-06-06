package com.cloud.pseudocode.zuul;

public interface FilterFactory {

    ZuulFilter newInstance(Class var1) throws Exception;
}
