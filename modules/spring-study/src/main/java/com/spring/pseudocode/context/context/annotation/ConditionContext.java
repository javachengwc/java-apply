package com.spring.pseudocode.context.context.annotation;

import com.spring.pseudocode.beans.factory.config.ConfigurableListableBeanFactory;
import com.spring.pseudocode.beans.factory.support.BeanDefinitionRegistry;
import com.spring.pseudocode.core.core.env.Environment;
import com.spring.pseudocode.core.core.io.ResourceLoader;

public abstract interface ConditionContext
{
    public abstract BeanDefinitionRegistry getRegistry();

    public abstract ConfigurableListableBeanFactory getBeanFactory();

    public abstract Environment getEnvironment();

    public abstract ResourceLoader getResourceLoader();

    public abstract ClassLoader getClassLoader();
}
