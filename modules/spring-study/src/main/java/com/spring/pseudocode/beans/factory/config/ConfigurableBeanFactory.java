package com.spring.pseudocode.beans.factory.config;

import com.spring.pseudocode.beans.factory.BeanFactory;
import org.springframework.beans.factory.HierarchicalBeanFactory;

public abstract interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry
{
    public static final String SCOPE_SINGLETON = "singleton";
    public static final String SCOPE_PROTOTYPE = "prototype";

    public abstract void setParentBeanFactory(BeanFactory beanFactory) throws IllegalStateException;

    public abstract void setBeanClassLoader(ClassLoader classLoader);

    public abstract ClassLoader getBeanClassLoader();

    public abstract void setTempClassLoader(ClassLoader paramClassLoader);

    public abstract ClassLoader getTempClassLoader();

    public abstract void setCacheBeanMetadata(boolean paramBoolean);

    public abstract boolean isCacheBeanMetadata();

    public abstract String resolveEmbeddedValue(String paramString);

    public abstract void addBeanPostProcessor(BeanPostProcessor paramBeanPostProcessor);

    public abstract int getBeanPostProcessorCount();

    public abstract void registerScope(String paramString, Scope paramScope);

    public abstract String[] getRegisteredScopeNames();

    public abstract Scope getRegisteredScope(String paramString);

    public abstract void setCurrentlyInCreation(String paramString, boolean paramBoolean);

    public abstract boolean isCurrentlyInCreation(String paramString);

    public abstract void registerDependentBean(String paramString1, String paramString2);

    public abstract String[] getDependentBeans(String paramString);

    public abstract String[] getDependenciesForBean(String paramString);

    public abstract void destroyBean(String paramString, Object paramObject);

    public abstract void destroyScopedBean(String paramString);

    public abstract void destroySingletons();
}
