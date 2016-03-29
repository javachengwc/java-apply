package com.util.aop.advice;

import java.lang.reflect.Method;

/**
 * 前置增强
 *
 */
public interface BeforeAdvice extends Advice{

	public void before(Method method, Object[] args, Object target)throws Throwable;
	
}
