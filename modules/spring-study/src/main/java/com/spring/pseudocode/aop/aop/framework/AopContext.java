package com.spring.pseudocode.aop.aop.framework;

import org.springframework.core.NamedThreadLocal;

public abstract class AopContext
{
    private static final ThreadLocal<Object> currentProxy = new NamedThreadLocal("Current AOP proxy");

    public static Object currentProxy() throws IllegalStateException
    {
        Object proxy = currentProxy.get();
        if (proxy == null) {
            throw new IllegalStateException("Cannot find current proxy: Set 'exposeProxy' property on Advised to 'true' to make it available.");
        }

        return proxy;
    }

    public static Object setCurrentProxy(Object proxy)
    {
        Object old = currentProxy.get();
        if (proxy != null) {
            currentProxy.set(proxy);
        }
        else {
            currentProxy.remove();
        }
        return old;
    }
}
