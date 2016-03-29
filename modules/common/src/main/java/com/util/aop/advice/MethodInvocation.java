package com.util.aop.advice;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodProxy;

public class MethodInvocation {

	private MethodProxy proxy;
	private Method method;
	private Object[] args;
	private Object target;
	
	public MethodInvocation(MethodProxy proxy, Method method, Object[] args, Object target) {
		super();
		this.proxy = proxy;
		this.method = method;
		this.args = args;
		this.target = target;
	}
	
	public Object getThis(){
		return target;
	}
	
	public Method getMethod(){
		return method;
	}
	
	public Object[] getArguments(){
		return args;
	}
	
	public Object proceed()throws Throwable{
		if(proxy != null){
			return proxy.invokeSuper(target, args);
		} 
		return method.invoke(target, args);
	}
	
}
