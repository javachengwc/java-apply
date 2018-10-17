package com.spring.pseudocode.core.core.io;

import org.springframework.core.io.Resource;

import java.io.IOException;

public interface ResourcePatternResolver extends ResourceLoader {

    public static String CLASSPATH_ALL_URL_PREFIX = "classpath*:";

    public Resource[] getResources(String locationPattern) throws IOException;

}
