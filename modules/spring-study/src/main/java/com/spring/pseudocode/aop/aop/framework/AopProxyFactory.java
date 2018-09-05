package com.spring.pseudocode.aop.aop.framework;

public abstract interface AopProxyFactory
{
    public abstract AopProxy createAopProxy(AdvisedSupport advisedSupport) throws AopConfigException;
}
