package com.spring.pseudocode.aop.aop;

import java.lang.reflect.Method;

public abstract interface IntroductionAwareMethodMatcher extends MethodMatcher
{
    public abstract boolean matches(Method method, Class<?> paramClass, boolean bool);
}
