package com.util.aop.pointcut;

import java.lang.reflect.Method;


public class PointcutImpl implements Pointcut {
	
	private String regex;
	
	public PointcutImpl(){
		
	}
	
	public PointcutImpl(String regex){
		this.regex = regex;
	}

	@Override
	public boolean matches(Method method, Object[] args, Object target) {
		if(regex == null){
			return true;
		}
		return method.getName().matches(regex);
	}

}
