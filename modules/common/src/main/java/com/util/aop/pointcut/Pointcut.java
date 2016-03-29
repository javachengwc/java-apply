package com.util.aop.pointcut;

import java.lang.reflect.Method;

public interface Pointcut {

	public boolean matches(Method method, Object[] args, Object target);
	
}
