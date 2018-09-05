package com.spring.pseudocode.aop.aop.framework;

import com.spring.pseudocode.aop.aop.SpringProxy;
import com.spring.pseudocode.aop.aop.TargetClassAware;
import com.spring.pseudocode.aop.aop.TargetSource;
import com.spring.pseudocode.core.core.DecoratingProxy;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.target.SingletonTargetSource;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public abstract class AopProxyUtils
{
    public static Class<?> ultimateTargetClass(Object candidate)
    {
        Object current = candidate;
        Class result = null;
        while ((current instanceof TargetClassAware)) {
            result = ((TargetClassAware)current).getTargetClass();
            Object nested = null;
            if ((current instanceof Advised)) {
                TargetSource targetSource = ((Advised)current).getTargetSource();
                if ((targetSource instanceof SingletonTargetSource)) {
                    nested = ((SingletonTargetSource)targetSource).getTarget();
                }
            }
            current = nested;
        }
        if (result == null) {
            result = AopUtils.isCglibProxy(candidate) ? candidate.getClass().getSuperclass() : candidate.getClass();
        }
        return result;
    }

    public static Class<?>[] completeProxiedInterfaces(AdvisedSupport advised)
    {
        return completeProxiedInterfaces(advised, false);
    }

    static Class<?>[] completeProxiedInterfaces(AdvisedSupport advised, boolean decoratingProxy)
    {
        Class[] specifiedInterfaces = advised.getProxiedInterfaces();
        if (specifiedInterfaces.length == 0)
        {
            Class targetClass = advised.getTargetClass();
            if (targetClass != null) {
                if (targetClass.isInterface()) {
                    advised.setInterfaces(new Class[] { targetClass });
                }
                else if (Proxy.isProxyClass(targetClass)) {
                    advised.setInterfaces(targetClass.getInterfaces());
                }
                specifiedInterfaces = advised.getProxiedInterfaces();
            }
        }
        boolean addSpringProxy = !advised.isInterfaceProxied(SpringProxy.class);
        boolean addAdvised = (!advised.isOpaque()) && (!advised.isInterfaceProxied(Advised.class));
        boolean addDecoratingProxy = (decoratingProxy) && (!advised.isInterfaceProxied(DecoratingProxy.class));
        int nonUserIfcCount = 0;
        if (addSpringProxy) {
            nonUserIfcCount++;
        }
        if (addAdvised) {
            nonUserIfcCount++;
        }
        if (addDecoratingProxy) {
            nonUserIfcCount++;
        }
        Class[] proxiedInterfaces = new Class[specifiedInterfaces.length + nonUserIfcCount];
        System.arraycopy(specifiedInterfaces, 0, proxiedInterfaces, 0, specifiedInterfaces.length);
        int index = specifiedInterfaces.length;
        if (addSpringProxy) {
            proxiedInterfaces[index] = SpringProxy.class;
            index++;
        }
        if (addAdvised) {
            proxiedInterfaces[index] = Advised.class;
            index++;
        }
        if (addDecoratingProxy) {
            proxiedInterfaces[index] = DecoratingProxy.class;
        }
        return proxiedInterfaces;
    }

    public static Class<?>[] proxiedUserInterfaces(Object proxy)
    {
        Class[] proxyInterfaces = proxy.getClass().getInterfaces();
        int nonUserIfcCount = 0;
        if ((proxy instanceof SpringProxy)) {
            nonUserIfcCount++;
        }
        if ((proxy instanceof Advised)) {
            nonUserIfcCount++;
        }
        if ((proxy instanceof DecoratingProxy)) {
            nonUserIfcCount++;
        }
        Class[] userInterfaces = new Class[proxyInterfaces.length - nonUserIfcCount];
        System.arraycopy(proxyInterfaces, 0, userInterfaces, 0, userInterfaces.length);
        return userInterfaces;
    }

    public static boolean equalsInProxy(AdvisedSupport a, AdvisedSupport b)
    {
        return (a == b) || (
                (equalsProxiedInterfaces(a, b)) &&
                        (equalsAdvisors(a, b)) && (a.getTargetSource().equals(b.getTargetSource())));
    }

    public static boolean equalsProxiedInterfaces(AdvisedSupport a, AdvisedSupport b)
    {
        return Arrays.equals(a.getProxiedInterfaces(), b.getProxiedInterfaces());
    }

    public static boolean equalsAdvisors(AdvisedSupport a, AdvisedSupport b)
    {
        return Arrays.equals(a.getAdvisors(), b.getAdvisors());
    }

    static Object[] adaptArgumentsIfNecessary(Method method, Object[] arguments)
    {
        if ((method.isVarArgs()) && (!ObjectUtils.isEmpty(arguments))) {
            Class[] paramTypes = method.getParameterTypes();
            if (paramTypes.length == arguments.length) {
                int varargIndex = paramTypes.length - 1;
                Class varargType = paramTypes[varargIndex];
                if (varargType.isArray()) {
                    Object varargArray = arguments[varargIndex];
                    if (((varargArray instanceof Object[])) && (!varargType.isInstance(varargArray))) {
                        Object[] newArguments = new Object[arguments.length];
                        System.arraycopy(arguments, 0, newArguments, 0, varargIndex);
                        Class targetElementType = varargType.getComponentType();
                        int varargLength = Array.getLength(varargArray);
                        Object newVarargArray = Array.newInstance(targetElementType, varargLength);
                        System.arraycopy(varargArray, 0, newVarargArray, 0, varargLength);
                        newArguments[varargIndex] = newVarargArray;
                        return newArguments;
                    }
                }
            }
        }
        return arguments;
    }
}
