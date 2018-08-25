package com.spring.pseudocode.beans.factory;

//BeanFactory 是Spring bean容器的根接口.提供获取bean,是否包含bean,是否单例与原型,获取bean类型,bean 别名的api
//BeanFactory是一个Bean工厂类，完成Bean的初始化操作。
//Bean的初始化操作还是一个比较麻烦的操作，首先根据spring注入配置将bean初始化为单例或者原型，
//其次需要根据Bean的属性配置来完成Bean参数的注入配置，还有要解决单例模式下Bean的循环依赖的问题，原型模式下bean的循环依赖会直接报错。
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
