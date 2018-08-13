package com.spring.pseudocode.context.context;


import com.spring.pseudocode.beans.factory.AutowireCapableBeanFactory;
import com.spring.pseudocode.beans.factory.BeanFactory;

public abstract interface ApplicationContext extends BeanFactory {

    public abstract String getId();

    public abstract String getApplicationName();

    public abstract String getDisplayName();

    public abstract long getStartupDate();

    public abstract ApplicationContext getParent();

    public abstract AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException;
}
