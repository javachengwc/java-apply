package com.pseudocode.cloud.commons.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public abstract class SpringFactoryImportSelector<T> implements DeferredImportSelector, BeanClassLoaderAware, EnvironmentAware {

    private final Log log = LogFactory.getLog(SpringFactoryImportSelector.class);

    private ClassLoader beanClassLoader;

    private Class<T> annotationClass;

    private Environment environment;

    @SuppressWarnings("unchecked")
    protected SpringFactoryImportSelector() {
        this.annotationClass = (Class<T>) GenericTypeResolver.resolveTypeArgument(this.getClass(), SpringFactoryImportSelector.class);
    }

    @Override
    public String[] selectImports(AnnotationMetadata metadata) {
        if (!isEnabled()) {
            return new String[0];
        }
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                metadata.getAnnotationAttributes(this.annotationClass.getName(), true));

        List<String> factories = new ArrayList<>(new LinkedHashSet<>(SpringFactoriesLoader
                .loadFactoryNames(this.annotationClass, this.beanClassLoader)));

        return factories.toArray(new String[factories.size()]);
    }

    protected boolean hasDefaultFactory() {
        return false;
    }

    protected abstract boolean isEnabled();

    protected String getSimpleName() {
        return this.annotationClass.getSimpleName();
    }

    protected Class<T> getAnnotationClass() {
        return this.annotationClass;
    }

    protected Environment getEnvironment() {
        return this.environment;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

}
