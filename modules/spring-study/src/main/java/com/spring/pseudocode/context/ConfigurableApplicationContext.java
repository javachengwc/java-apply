package com.spring.pseudocode.context;

import com.spring.pseudocode.web.web.context.Lifecycle;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.Closeable;

public abstract interface ConfigurableApplicationContext extends ApplicationContext, Lifecycle, Closeable
{

    public abstract void setId(String paramString);

    public abstract void setParent(ApplicationContext paramApplicationContext);

    public abstract ConfigurableEnvironment getEnvironment();

    public abstract void setEnvironment(ConfigurableEnvironment paramConfigurableEnvironment);

    public abstract void addApplicationListener(ApplicationListener<?> paramApplicationListener);

    public abstract void refresh() throws Exception;

    public abstract void registerShutdownHook();

    public abstract void close();

    public abstract boolean isActive();

    public abstract ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;
}
