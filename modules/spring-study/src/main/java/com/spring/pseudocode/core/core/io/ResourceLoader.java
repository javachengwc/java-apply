package com.spring.pseudocode.core.core.io;

//读取Resource
public abstract interface ResourceLoader
{
    public static final String CLASSPATH_URL_PREFIX = "classpath:";

    public abstract Resource getResource(String paramString);

    public abstract ClassLoader getClassLoader();
}
