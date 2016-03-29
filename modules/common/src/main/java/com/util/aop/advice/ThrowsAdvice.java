package com.util.aop.advice;

/**
 * 异常增强
 *
 */
public interface ThrowsAdvice extends Advice{

	public void afterThrowing(Throwable t)throws Throwable;
	
}
