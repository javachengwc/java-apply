package com.spring.pseudocode.beans.factory.config;

public abstract interface BeanDefinition
{
    public static final String SCOPE_SINGLETON = "singleton";

    public static final String SCOPE_PROTOTYPE = "prototype";

    public abstract void setBeanClassName(String param);

    public abstract String getBeanClassName();

    public abstract void setScope(String param);

    public abstract String getScope();

    public abstract void setLazyInit(boolean param);

    public abstract boolean isLazyInit();

    public abstract void setDependsOn(String[] param);

    public abstract String[] getDependsOn();

    public abstract void setAutowireCandidate(boolean param);

    public abstract boolean isAutowireCandidate();

    public abstract void setPrimary(boolean param);

    public abstract boolean isPrimary();

    public abstract void setFactoryBeanName(String param);

    public abstract String getFactoryBeanName();

    public abstract void setFactoryMethodName(String param);

    public abstract String getFactoryMethodName();

    public abstract boolean isSingleton();

    public abstract boolean isPrototype();

    public abstract boolean isAbstract();

    public abstract Object getSource();

}
