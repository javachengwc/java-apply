package com.util.aop.advice;

import java.lang.reflect.Method;

/**
 * 后置增强
 *
 */
public interface AfterAdvice extends Advice{
	
	public void afterReturning(Object returnObj, Method method, Object[] args, Object target)throws Throwable;

}
