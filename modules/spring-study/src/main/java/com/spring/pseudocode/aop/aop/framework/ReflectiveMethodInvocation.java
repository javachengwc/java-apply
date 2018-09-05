package com.spring.pseudocode.aop.aop.framework;

import com.spring.pseudocode.aop.aop.ProxyMethodInvocation;
import com.spring.pseudocode.aop.aop.support.AopUtils;
import com.spring.pseudocode.aop.aopalliance.intercept.MethodInterceptor;
import com.spring.pseudocode.aop.aopalliance.intercept.MethodInvocation;
import org.springframework.core.BridgeMethodResolver;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectiveMethodInvocation implements ProxyMethodInvocation, Cloneable
{
    protected final Object proxy;
    protected final Object target;
    protected final Method method;
    protected Object[] arguments;
    private final Class<?> targetClass;
    private Map<String, Object> userAttributes;
    //interceptorsAndDynamicMethodMatchers中包含了需要切入某个方法所有的Advice通知
    protected final List<?> interceptorsAndDynamicMethodMatchers;
    private int currentInterceptorIndex = -1;

    protected ReflectiveMethodInvocation(Object proxy, Object target, Method method, Object[] arguments, Class<?> targetClass, List<Object> interceptorsAndDynamicMethodMatchers)
    {
        this.proxy = proxy;
        this.target = target;
        this.targetClass = targetClass;
        this.method = BridgeMethodResolver.findBridgedMethod(method);
        this.arguments = AopProxyUtils.adaptArgumentsIfNecessary(method, arguments);
        this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;
    }

    public final Object getProxy()
    {
        return this.proxy;
    }

    public final Object getThis()
    {
        return this.target;
    }

    public final AccessibleObject getStaticPart()
    {
        return this.method;
    }

    public final Method getMethod()
    {
        return this.method;
    }

    public final Object[] getArguments()
    {
        return this.arguments != null ? this.arguments : new Object[0];
    }

    public void setArguments(Object[] arguments)
    {
        this.arguments = arguments;
    }

    public Object proceed() throws Throwable
    {
        //interceptorsAndDynamicMethodMatchers所有通知的链表
        if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
            //如果所有的advice都已经进行处理就可以递归执行方法了
            return invokeJoinpoint();
        }

        //每次获取一个advice
        Object interceptorOrInterceptionAdvice = this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);

//        if ((interceptorOrInterceptionAdvice instanceof InterceptorAndDynamicMethodMatcher))
//        {
//            InterceptorAndDynamicMethodMatcher dm = (InterceptorAndDynamicMethodMatcher)interceptorOrInterceptionAdvice;
//
//            if (dm.methodMatcher.matches(this.method, this.targetClass, this.arguments)) {
//                return dm.interceptor.invoke(this);
//            }
//
//            return proceed();
//        }

        //调用的Advice的invoke方法时会递归调用proceed方法
        return ((MethodInterceptor)interceptorOrInterceptionAdvice).invoke(this);
    }

    protected Object invokeJoinpoint() throws Throwable
    {
        return AopUtils.invokeJoinpointUsingReflection(this.target, this.method, this.arguments);
    }

    public MethodInvocation invocableClone()
    {
        Object[] cloneArguments = null;
        if (this.arguments != null)
        {
            cloneArguments = new Object[this.arguments.length];
            System.arraycopy(this.arguments, 0, cloneArguments, 0, this.arguments.length);
        }
        return invocableClone(cloneArguments);
    }

    public MethodInvocation invocableClone(Object[] arguments)
    {
        if (this.userAttributes == null) {
            this.userAttributes = new HashMap();
        }

        try
        {
            ReflectiveMethodInvocation clone = (ReflectiveMethodInvocation)clone();
            clone.arguments = arguments;
            return clone;
        }
        catch (CloneNotSupportedException ex) {
            throw new IllegalStateException(new StringBuilder().append("Should be able to clone object of type [")
                    .append(getClass()).append("]: ").append(ex).toString());
        }
    }

    public void setUserAttribute(String key, Object value)
    {
        if (value != null) {
            if (this.userAttributes == null) {
                this.userAttributes = new HashMap();
            }
            this.userAttributes.put(key, value);
        }
        else if (this.userAttributes != null) {
            this.userAttributes.remove(key);
        }
    }

    public Object getUserAttribute(String key)
    {
        return this.userAttributes != null ? this.userAttributes.get(key) : null;
    }

    public Map<String, Object> getUserAttributes()
    {
        if (this.userAttributes == null) {
            this.userAttributes = new HashMap();
        }
        return this.userAttributes;
    }
}
