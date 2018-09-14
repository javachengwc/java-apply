package com.boot.pseudocode.autoconfigure;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;
import java.util.*;

public class AutoConfigurationImportSelector implements DeferredImportSelector, BeanClassLoaderAware,
        ResourceLoaderAware, BeanFactoryAware, EnvironmentAware, Ordered
{
    private static final String[] NO_IMPORTS = new String[0];

    private static final Log logger = LogFactory.getLog(AutoConfigurationImportSelector.class);

    private ConfigurableListableBeanFactory beanFactory;

    private Environment environment;

    private ClassLoader beanClassLoader;

    private ResourceLoader resourceLoader;

    public String[] selectImports(AnnotationMetadata annotationMetadata)
    {
        if (!isEnabled(annotationMetadata)) {
            return NO_IMPORTS;
        }
        try
        {
            AutoConfigurationMetadata autoConfigurationMetadata = AutoConfigurationMetadataLoader.loadMetadata(this.beanClassLoader);

            AnnotationAttributes attributes = getAttributes(annotationMetadata);
            List configurations = getCandidateConfigurations(annotationMetadata, attributes);

//            configurations = removeDuplicates(configurations);
//            configurations = sort(configurations, autoConfigurationMetadata);
//            Set exclusions = getExclusions(annotationMetadata, attributes);
//            checkExcludedClasses(configurations, exclusions);
//            configurations.removeAll(exclusions);
//            configurations = filter(configurations, autoConfigurationMetadata);
//            fireAutoConfigurationImportEvents(configurations, exclusions);
            return (String[])configurations.toArray(new String[configurations.size()]);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    protected boolean isEnabled(AnnotationMetadata metadata)
    {
        return true;
    }

    protected AnnotationAttributes getAttributes(AnnotationMetadata metadata)
    {
        String name = getAnnotationClass().getName();
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(name, true));
        return attributes;
    }

    protected Class<?> getAnnotationClass()
    {
        return EnableAutoConfiguration.class;
    }

    protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes)
    {
        List configurations = SpringFactoriesLoader.loadFactoryNames(
                getSpringFactoriesLoaderFactoryClass(), getBeanClassLoader());

        return configurations;
    }

    protected Class<?> getSpringFactoriesLoaderFactoryClass()
    {
        return EnableAutoConfiguration.class;
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException
    {
        this.beanFactory = ((ConfigurableListableBeanFactory)beanFactory);
    }

    protected final ConfigurableListableBeanFactory getBeanFactory() {
        return this.beanFactory;
    }

    public void setBeanClassLoader(ClassLoader classLoader)
    {
        this.beanClassLoader = classLoader;
    }

    protected ClassLoader getBeanClassLoader() {
        return this.beanClassLoader;
    }

    public void setEnvironment(Environment environment)
    {
        this.environment = environment;
    }

    protected final Environment getEnvironment() {
        return this.environment;
    }

    public void setResourceLoader(ResourceLoader resourceLoader)
    {
        this.resourceLoader = resourceLoader;
    }

    protected final ResourceLoader getResourceLoader() {
        return this.resourceLoader;
    }

    public int getOrder()
    {
        return Integer.MAX_VALUE;
    }
}
