package com.cloud.pseudocode.zuul;

public class DefaultFilterFactory implements FilterFactory {

    public DefaultFilterFactory() {
    }

    public ZuulFilter newInstance(Class clazz) throws InstantiationException, IllegalAccessException {
        return (ZuulFilter)clazz.newInstance();
    }
}
