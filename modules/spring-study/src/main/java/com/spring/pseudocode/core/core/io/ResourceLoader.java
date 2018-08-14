package com.spring.pseudocode.core.core.io;

public abstract interface ResourceLoader
{
    public static final String CLASSPATH_URL_PREFIX = "classpath:";

    public abstract Resource getResource(String paramString);

    public abstract ClassLoader getClassLoader();
}
