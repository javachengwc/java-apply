package com.spring.pseudocode.beans.factory.support;

import com.spring.pseudocode.beans.factory.BeanDefinitionStoreException;
import com.spring.pseudocode.core.core.io.Resource;
import com.spring.pseudocode.core.core.io.ResourceLoader;

public abstract interface BeanDefinitionReader
{
    public abstract BeanDefinitionRegistry getRegistry();

    public abstract ResourceLoader getResourceLoader();

    public abstract ClassLoader getBeanClassLoader();

    public abstract BeanNameGenerator getBeanNameGenerator();

    public abstract int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException;

    public abstract int loadBeanDefinitions(Resource[] resources) throws BeanDefinitionStoreException;

    public abstract int loadBeanDefinitions(String param) throws BeanDefinitionStoreException;

    public abstract int loadBeanDefinitions(String[] params) throws BeanDefinitionStoreException;
}