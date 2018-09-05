package com.spring.pseudocode.aop.aop;

import com.spring.pseudocode.aop.aopalliance.aop.Advice;

//切面
//Advisor接口及其实现类是Advice(通知)和PointCut(切入点)的一个组合体，就是一个Aspect切面
//配置文件中<aop:aspect>代表一个切面Advisor集合
public abstract interface Advisor
{
    public abstract Advice getAdvice();

    public abstract boolean isPerInstance();
}
