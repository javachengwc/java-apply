package com.spring.pseudocode.core.core.io;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ContextResource;
import org.springframework.core.io.ProtocolResolver;
import org.springframework.core.io.UrlResource;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class DefaultResourceLoader implements ResourceLoader
{
    private ClassLoader classLoader;
    private final Set<ProtocolResolver> protocolResolvers = new LinkedHashSet(4);

    public DefaultResourceLoader()
    {
        this.classLoader = ClassUtils.getDefaultClassLoader();
    }

    public DefaultResourceLoader(ClassLoader classLoader)
    {
        this.classLoader = classLoader;
    }

    public void setClassLoader(ClassLoader classLoader)
    {
        this.classLoader = classLoader;
    }

    public ClassLoader getClassLoader()
    {
        return this.classLoader != null ? this.classLoader : ClassUtils.getDefaultClassLoader();
    }

    public void addProtocolResolver(ProtocolResolver resolver)
    {
        this.protocolResolvers.add(resolver);
    }

    public Collection<ProtocolResolver> getProtocolResolvers()
    {
        return this.protocolResolvers;
    }

    public Resource getResource(String location)
    {
        for (ProtocolResolver protocolResolver : this.protocolResolvers) {
            //Resource resource = protocolResolver.resolve(location, this);
            Resource resource =null;
            if (resource != null) {
                return resource;
            }
        }

        if (location.startsWith("/")) {
            return getResourceByPath(location);
        }
        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            //return new ClassPathResource(location.substring("classpath:".length()), getClassLoader());
            return null;
        }

        try
        {
            URL url = new URL(location);
            //return new UrlResource(url);
            return null;
        }
        catch (MalformedURLException ex) {
        }
        return getResourceByPath(location);
    }

    protected Resource getResourceByPath(String path)
    {
        //return new ClassPathContextResource(path, getClassLoader());
        return null;
    }
}