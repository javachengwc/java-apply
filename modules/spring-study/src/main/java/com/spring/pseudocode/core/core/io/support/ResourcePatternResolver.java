package com.spring.pseudocode.core.core.io.support;

import com.spring.pseudocode.core.core.io.Resource;
import com.spring.pseudocode.core.core.io.ResourceLoader;

import java.io.IOException;

public abstract interface ResourcePatternResolver extends ResourceLoader
{
    public static final String CLASSPATH_ALL_URL_PREFIX = "classpath*:";

    public abstract Resource[] getResources(String param) throws IOException;
}
