package com.spring.pseudocode.beans.factory.support;

import com.spring.pseudocode.beans.BeansException;
import com.spring.pseudocode.beans.factory.BeanFactory;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class SimpleInstantiationStrategy implements InstantiationStrategy
{
    private static final ThreadLocal<Method> currentlyInvokedFactoryMethod = new ThreadLocal();

    public static Method getCurrentlyInvokedFactoryMethod()
    {
        return (Method)currentlyInvokedFactoryMethod.get();
    }

    public Object instantiate(RootBeanDefinition bd, String beanName, BeanFactory owner)
    {
        if (bd.getMethodOverrides().isEmpty())
        {
            Constructor<?> constructorToUse;
            synchronized (bd.constructorArgumentLock) {
                constructorToUse = (Constructor)bd.resolvedConstructorOrFactoryMethod;
                if (constructorToUse == null) {
                    Class clazz = bd.getBeanClass();
                    if (clazz.isInterface())
                        throw new BeanInstantiationException(clazz, "Specified class is an interface");
                    try
                    {
                        //...
                        constructorToUse = clazz.getDeclaredConstructor((Class[])null);
                        bd.resolvedConstructorOrFactoryMethod = constructorToUse;
                    }
                    catch (Throwable ex) {
                        throw new BeanInstantiationException(clazz, "No default constructor found", ex);
                    }
                }
            }
            //最终在BeanUtils中通过反射机制完成Bean的初始化操作
            return BeanUtils.instantiateClass(constructorToUse, new Object[0]);
        }
        // Must generate CGLIB subclass
        return instantiateWithMethodInjection(bd, beanName, owner);
    }

    @Override
    public Object instantiate(RootBeanDefinition rootBeanDefinition, String param, BeanFactory beanFactory,
                              Constructor<?> constructor, Object[] arrayOfObject) throws BeansException {
        return null;
    }

    @Override
    public Object instantiate(RootBeanDefinition rootBeanDefinition, String param, BeanFactory beanFactory,
                              Object object, Method method, Object[] arrayOfObject) throws BeansException {
        return null;
    }

    protected Object instantiateWithMethodInjection(RootBeanDefinition bd, String beanName, BeanFactory owner)
    {
        //...
        return null;
    }

    //BeanUtils.instantiateClass的实现
    public static <T> T instantiateClass(Constructor<T> ctor, Object... args) throws BeanInstantiationException {
        try {
            ReflectionUtils.makeAccessible(ctor);
            //通过反射机制初始化bean
            return ctor.newInstance(args);
        } catch (Exception ex) {
            //...
        }
        return null;
    }
}
