package com.spring.pseudocode.aop.aopalliance.intercept;

//所有的advice都要最终转化成MethodInterceptor
public abstract interface MethodInterceptor extends Interceptor
{
    //invoke方法包含了拦截器要执行的内容及执行的顺序
    public abstract Object invoke(MethodInvocation methodInvocation)  throws Throwable;
}
