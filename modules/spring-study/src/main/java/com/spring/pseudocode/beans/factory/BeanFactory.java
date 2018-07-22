package com.spring.pseudocode.beans.factory;


public abstract interface BeanFactory
{

    public abstract Object getBean(String paramString);

    public abstract <T> T getBean(String paramString, Class<T> paramClass);

    public abstract <T> T getBean(Class<T> paramClass);

    public abstract Object getBean(String paramString, Object[] paramArrayOfObject);

    public abstract <T> T getBean(Class<T> paramClass, Object[] paramArrayOfObject);

    public abstract boolean containsBean(String paramString);

    public abstract boolean isSingleton(String paramString);

    public abstract boolean isPrototype(String paramString);

    public abstract boolean isTypeMatch(String paramString, Class<?> paramClass);

    public abstract Class<?> getType(String paramString);

    public abstract String[] getAliases(String paramString);
}
