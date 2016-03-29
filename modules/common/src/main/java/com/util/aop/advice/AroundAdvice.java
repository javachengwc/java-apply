package com.util.aop.advice;

/**
 * 环绕增强
 *
 */
public interface AroundAdvice extends Advice{

	public Object around(MethodInvocation invocation)throws Throwable;
	
}
