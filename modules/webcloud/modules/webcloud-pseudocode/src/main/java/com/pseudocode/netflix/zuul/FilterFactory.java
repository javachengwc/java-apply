package com.pseudocode.netflix.zuul;

import com.pseudocode.netflix.zuul.ZuulFilter;

public interface FilterFactory {

    ZuulFilter newInstance(Class var1) throws Exception;
}
