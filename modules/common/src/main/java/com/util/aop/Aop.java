package com.util.aop;

import java.lang.reflect.Method;

public interface Aop {
		
	public void before(Method method, Object[] args) throws Exception;

	public void after(Method method, Object[] args) throws Exception;
}
