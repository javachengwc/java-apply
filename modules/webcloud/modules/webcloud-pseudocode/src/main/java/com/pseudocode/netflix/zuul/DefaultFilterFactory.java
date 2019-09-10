package com.pseudocode.netflix.zuul;

import com.pseudocode.netflix.zuul.FilterFactory;
import com.pseudocode.netflix.zuul.ZuulFilter;

public class DefaultFilterFactory implements FilterFactory {

    public DefaultFilterFactory() {
    }

    public ZuulFilter newInstance(Class clazz) throws InstantiationException, IllegalAccessException {
        return (ZuulFilter)clazz.newInstance();
    }
}
