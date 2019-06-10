package com.pseudocode.cloud.zuul;

public interface FilterFactory {

    ZuulFilter newInstance(Class var1) throws Exception;
}
