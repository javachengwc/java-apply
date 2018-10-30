package com.spring.pseudocode.aop.aop.framework;

import com.spring.pseudocode.aop.aop.AopInvocationException;
import com.spring.pseudocode.aop.aop.TargetSource;
import com.spring.pseudocode.aop.aop.support.AopUtils;
import com.spring.pseudocode.aop.aopalliance.intercept.MethodInvocation;
import com.spring.pseudocode.core.core.DecoratingProxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.RawTargetAccess;
import org.springframework.util.ClassUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

//jdk代理
public final class JdkDynamicAopProxy implements AopProxy, InvocationHandler, Serializable
{
    private static final long serialVersionUID = 5531744639992436476L;
    private static final Log logger = LogFactory.getLog(JdkDynamicAopProxy.class);
    private final AdvisedSupport advised;
    private boolean equalsDefined;
    private boolean hashCodeDefined;

    public JdkDynamicAopProxy(AdvisedSupport config) throws AopConfigException
    {
        if ((config.getAdvisors().length == 0) && (config.getTargetSource() == AdvisedSupport.EMPTY_TARGET_SOURCE)) {
            throw new AopConfigException("No advisors and no TargetSource specified");
        }
        this.advised = config;
    }

    public Object getProxy()
    {
        return getProxy(ClassUtils.getDefaultClassLoader());
    }

    public Object getProxy(ClassLoader classLoader)
    {
        if (logger.isDebugEnabled()) {
            logger.debug("Creating JDK dynamic proxy: target source is " + this.advised.getTargetSource());
        }
        Class[] proxiedInterfaces = AopProxyUtils.completeProxiedInterfaces(this.advised, true);
        findDefinedEqualsAndHashCodeMethods(proxiedInterfaces);
        return Proxy.newProxyInstance(classLoader, proxiedInterfaces, this);
    }

    private void findDefinedEqualsAndHashCodeMethods(Class<?>[] proxiedInterfaces)
    {
        for (Class proxiedInterface : proxiedInterfaces) {
            Method[] methods = proxiedInterface.getDeclaredMethods();
            for (Method method : methods) {
                if (AopUtils.isEqualsMethod(method)) {
                    this.equalsDefined = true;
                }
                if (AopUtils.isHashCodeMethod(method)) {
                    this.hashCodeDefined = true;
                }
                if ((this.equalsDefined) && (this.hashCodeDefined))
                    return;
            }
        }
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        Object oldProxy = null;
        boolean setProxyContext = false;

        TargetSource targetSource = this.advised.targetSource;
        Class targetClass = null;
        Object target = null;
        try
        {
            Object localObject1;
            if ((!this.equalsDefined) && (AopUtils.isEqualsMethod(method)))
            {
                localObject1 = Boolean.valueOf(equals(args[0]));
                return localObject1;
            }
            if ((!this.hashCodeDefined) && (AopUtils.isHashCodeMethod(method)))
            {
                localObject1 = Integer.valueOf(hashCode());
                return localObject1;
            }
            if (method.getDeclaringClass() == DecoratingProxy.class)
            {
                localObject1 = AopProxyUtils.ultimateTargetClass(this.advised);
                return localObject1;
            }
            if ((!this.advised.opaque) && (method.getDeclaringClass().isInterface()) &&
                    (method
                            .getDeclaringClass().isAssignableFrom(Advised.class)))
            {
                localObject1 = AopUtils.invokeJoinpointUsingReflection(this.advised, method, args);
                return localObject1;
            }

            if (this.advised.exposeProxy)
            {
                oldProxy = AopContext.setCurrentProxy(proxy);
                setProxyContext = true;
            }

            target = targetSource.getTarget();
            if (target != null) {
                targetClass = target.getClass();
            }

            List chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
            Object retVal;
            if (chain.isEmpty())
            {
                Object[] argsToUse = AopProxyUtils.adaptArgumentsIfNecessary(method, args);
                retVal = AopUtils.invokeJoinpointUsingReflection(target, method, argsToUse);
            }
            else
            {
                MethodInvocation invocation = new ReflectiveMethodInvocation(proxy, target, method, args, targetClass, chain);
                retVal = invocation.proceed();
            }
            Class returnType = method.getReturnType();
            if ((retVal != null) && (retVal == target) && (returnType != Object.class) &&
                    (returnType.isInstance(proxy)) &&
                    (!RawTargetAccess.class.isAssignableFrom(method.getDeclaringClass())))
            {
                retVal = proxy;
            }
            else if ((retVal == null) && (returnType != Void.TYPE) && (returnType.isPrimitive())) {
                throw new AopInvocationException("Null return value from advice does not match primitive return type for: " + method);
            }

            Object localObject2 = retVal;
            return localObject2;
        }
        finally {
            if ((target != null) && (!targetSource.isStatic()))
            {
                targetSource.releaseTarget(target);
            }
            if (setProxyContext)
            {
                AopContext.setCurrentProxy(oldProxy);
            }
        }
    }

    public boolean equals(Object other)
    {
        if (other == this) {
            return true;
        }
        if (other == null)
            return false;
        JdkDynamicAopProxy otherProxy;
        if ((other instanceof JdkDynamicAopProxy)) {
            otherProxy = (JdkDynamicAopProxy)other;
        }
        else
        {
            if (Proxy.isProxyClass(other.getClass())) {
                InvocationHandler ih = Proxy.getInvocationHandler(other);
                if (!(ih instanceof JdkDynamicAopProxy)) {
                    return false;
                }
                otherProxy = (JdkDynamicAopProxy)ih;
            }
            else
            {
                return false;
            }
        }
        return AopProxyUtils.equalsInProxy(this.advised, otherProxy.advised);
    }

    public int hashCode()
    {
        return JdkDynamicAopProxy.class.hashCode() * 13 + this.advised.getTargetSource().hashCode();
    }
}