package com.spring.pseudocode.beans.factory.support;

import com.spring.pseudocode.beans.factory.BeanDefinitionStoreException;
import com.spring.pseudocode.core.core.env.Environment;
import com.spring.pseudocode.core.core.io.Resource;
import com.spring.pseudocode.core.core.io.ResourceLoader;
import com.spring.pseudocode.core.core.io.support.PathMatchingResourcePatternResolver;
import com.spring.pseudocode.core.core.io.support.ResourcePatternResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.Set;

public abstract class AbstractBeanDefinitionReader implements  BeanDefinitionReader
{
    protected final Log logger = LogFactory.getLog(getClass());

    private final BeanDefinitionRegistry registry;

    private ResourceLoader resourceLoader;

    private ClassLoader beanClassLoader;

    private Environment environment;

    protected AbstractBeanDefinitionReader(BeanDefinitionRegistry registry)
    {
        this.registry = registry;

        if (this.registry instanceof ResourceLoader) {
            this.resourceLoader = (ResourceLoader) this.registry;
        }
        else {
            //初始化xml资源读取器
            this.resourceLoader = new PathMatchingResourcePatternResolver();
        }
//
//        if (this.registry instanceof EnvironmentCapable) {
//            this.environment = ((EnvironmentCapable) this.registry).getEnvironment();
//        }
//        else {
//            this.environment = new StandardEnvironment();
//        }
    }

    public void setResourceLoader(ResourceLoader resourceLoader)
    {
        this.resourceLoader = resourceLoader;
    }

    public ResourceLoader getResourceLoader()
    {
        return this.resourceLoader;
    }

    public void setBeanClassLoader(ClassLoader beanClassLoader)
    {
        this.beanClassLoader = beanClassLoader;
    }

    public ClassLoader getBeanClassLoader()
    {
        return this.beanClassLoader;
    }

    public void setEnvironment(Environment environment)
    {
        this.environment = environment;
    }

    public Environment getEnvironment()
    {
        return this.environment;
    }

    public int loadBeanDefinitions(Resource[] resources) throws BeanDefinitionStoreException
    {
        int counter = 0;
        for (Resource resource : resources) {
            counter += loadBeanDefinitions(resource);
        }
        return counter;
    }

    public int loadBeanDefinitions(String location) throws BeanDefinitionStoreException
    {
        return loadBeanDefinitions(location, null);
    }

    public int loadBeanDefinitions(String location, Set<Resource> actualResources) throws BeanDefinitionStoreException
    {
        ResourceLoader resourceLoader = getResourceLoader();

        //判断资源地址类型
        if ((resourceLoader instanceof ResourcePatternResolver)) {
            try
            {
                Resource[] resources = ((ResourcePatternResolver)resourceLoader).getResources(location);
                //在子类的loadBeanDefinitions进行解析操作
                int loadCount = loadBeanDefinitions(resources);
                if (actualResources != null) {
                    for (Resource resource : resources) {
                        actualResources.add(resource);
                    }
                }
                return loadCount;
            }
            catch (IOException ex) {
                throw new BeanDefinitionStoreException("Could not resolve bean definition resource pattern [" + location + "]", ex);
            }

        }

        Resource resource = resourceLoader.getResource(location);
        int loadCount = loadBeanDefinitions(resource);
        if (actualResources != null) {
            actualResources.add(resource);
        }
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Loaded " + loadCount + " bean definitions from location [" + location + "]");
        }
        return loadCount;
    }

    public int loadBeanDefinitions(String[] locations) throws BeanDefinitionStoreException
    {
        int counter = 0;
        for (String location : locations) {
            counter += loadBeanDefinitions(location);
        }
        return counter;
    }
}