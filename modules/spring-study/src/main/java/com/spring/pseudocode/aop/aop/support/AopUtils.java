package com.spring.pseudocode.aop.aop.support;

import com.spring.pseudocode.aop.aop.*;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.MethodIntrospector;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.*;

public abstract class AopUtils
{
    public static boolean isAopProxy(Object object)
    {
        return ((object instanceof SpringProxy)) && (
                (Proxy.isProxyClass(object.getClass())) || (ClassUtils.isCglibProxyClass(object.getClass())));
    }

    public static boolean isJdkDynamicProxy(Object object)
    {
        return ((object instanceof SpringProxy)) && (Proxy.isProxyClass(object.getClass()));
    }

    public static boolean isCglibProxy(Object object)
    {
        return ((object instanceof SpringProxy)) && (ClassUtils.isCglibProxy(object));
    }

    public static Class<?> getTargetClass(Object candidate)
    {
        Class result = null;
        if ((candidate instanceof TargetClassAware)) {
            result = ((TargetClassAware)candidate).getTargetClass();
        }
        if (result == null) {
            result = isCglibProxy(candidate) ? candidate.getClass().getSuperclass() : candidate.getClass();
        }
        return result;
    }

    public static Method selectInvocableMethod(Method method, Class<?> targetType)
    {
        Method methodToUse = MethodIntrospector.selectInvocableMethod(method, targetType);
        if ((Modifier.isPrivate(methodToUse.getModifiers())) && (!Modifier.isStatic(methodToUse.getModifiers())) &&
                (SpringProxy.class.isAssignableFrom(targetType)))
        {
            throw new IllegalStateException(String.format("Need to invoke method '%s' found on proxy for target class '%s' " +
                    "but cannot be delegated to target bean. Switch its visibility to package or protected.",
                    new Object[] { method.getName(), method.getDeclaringClass().getSimpleName() }));
        }
        return methodToUse;
    }

    public static boolean isEqualsMethod(Method method)
    {
        return ReflectionUtils.isEqualsMethod(method);
    }

    public static boolean isHashCodeMethod(Method method)
    {
        return ReflectionUtils.isHashCodeMethod(method);
    }

    public static boolean isToStringMethod(Method method)
    {
        return ReflectionUtils.isToStringMethod(method);
    }

    public static boolean isFinalizeMethod(Method method)
    {
        return (method != null) && (method.getName().equals("finalize")) &&
                (method
                        .getParameterTypes().length == 0);
    }

    public static Method getMostSpecificMethod(Method method, Class<?> targetClass)
    {
        Method resolvedMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
        return BridgeMethodResolver.findBridgedMethod(resolvedMethod);
    }

    public static boolean canApply(Pointcut pc, Class<?> targetClass)
    {
        return canApply(pc, targetClass, false);
    }

    public static boolean canApply(Pointcut pc, Class<?> targetClass, boolean hasIntroductions)
    {
        if (!pc.getClassFilter().matches(targetClass)) {
            return false;
        }

        MethodMatcher methodMatcher = pc.getMethodMatcher();
        if (methodMatcher == MethodMatcher.TRUE)
        {
            return true;
        }

        IntroductionAwareMethodMatcher introductionAwareMethodMatcher = null;
        if ((methodMatcher instanceof IntroductionAwareMethodMatcher)) {
            introductionAwareMethodMatcher = (IntroductionAwareMethodMatcher)methodMatcher;
        }

        Set<Class> classes = new LinkedHashSet(ClassUtils.getAllInterfacesForClassAsSet(targetClass));
        classes.add(targetClass);
        for (Class clazz : classes) {
            Method[] methods = ReflectionUtils.getAllDeclaredMethods(clazz);
            for (Method method : methods) {
                if (((introductionAwareMethodMatcher != null) &&
                        (introductionAwareMethodMatcher.matches(method, targetClass, hasIntroductions))) ||
                        (methodMatcher
                                .matches(method, targetClass)))
                {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean canApply(Advisor advisor, Class<?> targetClass)
    {
        return canApply(advisor, targetClass, false);
    }

    public static boolean canApply(Advisor advisor, Class<?> targetClass, boolean hasIntroductions)
    {
        if ((advisor instanceof IntroductionAdvisor)) {
            return ((IntroductionAdvisor)advisor).getClassFilter().matches(targetClass);
        }
        if ((advisor instanceof PointcutAdvisor)) {
            PointcutAdvisor pca = (PointcutAdvisor)advisor;
            return canApply(pca.getPointcut(), targetClass, hasIntroductions);
        }

        return true;
    }

    public static List<Advisor> findAdvisorsThatCanApply(List<Advisor> candidateAdvisors, Class<?> clazz)
    {
        if (candidateAdvisors.isEmpty()) {
            return candidateAdvisors;
        }
        List eligibleAdvisors = new LinkedList();
        for (Iterator<Advisor> localIterator = candidateAdvisors.iterator(); localIterator.hasNext(); ) {
            Advisor candidate = (Advisor)localIterator.next();
            if (((candidate instanceof IntroductionAdvisor)) && (canApply(candidate, clazz))) {
                eligibleAdvisors.add(candidate);
            }
        }
        boolean hasIntroductions = !eligibleAdvisors.isEmpty();
        for (Advisor candidate : candidateAdvisors) {
            if ((candidate instanceof IntroductionAdvisor))
            {
                continue;
            }
            if (canApply(candidate, clazz, hasIntroductions)) {
                eligibleAdvisors.add(candidate);
            }
        }
        return eligibleAdvisors;
    }

    public static Object invokeJoinpointUsingReflection(Object target, Method method, Object[] args) throws Throwable
    {
        try
        {
            ReflectionUtils.makeAccessible(method);
            return method.invoke(target, args);
        }
        catch (InvocationTargetException ex)
        {
            throw ex.getTargetException();
        }
        catch (IllegalArgumentException ex) {
            throw new AopInvocationException("AOP configuration seems to be invalid: tried calling method [" + method + "] on target [" + target + "]", ex);
        }
        catch (IllegalAccessException ee) {
            throw new AopInvocationException("Could not access method [" + method + "]", ee);
        }

    }
}
