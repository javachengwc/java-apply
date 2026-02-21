package com.boot.pseudocode.autoconfigure;

import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

public class AutoConfigurationMetadataLoader {

    protected static final String PATH = "META-INF/spring-autoconfigure-metadata.properties";

    public static AutoConfigurationMetadata loadMetadata(ClassLoader classLoader)
    {
        return loadMetadata(classLoader, "META-INF/spring-autoconfigure-metadata.properties");
    }

    public static AutoConfigurationMetadata loadMetadata(ClassLoader classLoader, String path)
    {
        try {
            Enumeration urls = classLoader != null ? classLoader.getResources(path) : ClassLoader.getSystemResources(path);
            Properties properties = new Properties();
            while (urls.hasMoreElements()) {
                properties.putAll(PropertiesLoaderUtils.loadProperties(new UrlResource((URL)urls.nextElement())));
            }
            //AutoConfigurationMetadata metadata= loadMetadata(properties);
            AutoConfigurationMetadata metadata=null;
            return metadata;
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to load @ConditionalOnClass location [" + path + "]", ex);
        }
    }
}
