package com.spring.pseudocode.aop.aop;

import com.spring.pseudocode.aop.aopalliance.aop.Advice;

public abstract interface DynamicIntroductionAdvice extends Advice
{
    public abstract boolean implementsInterface(Class<?> paramClass);
}
