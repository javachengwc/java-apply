package com.flower.invocation;

import java.lang.reflect.Method;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.flower.BaseAction;
import com.flower.DataFilter;
import com.flower.interceptor.Interceptor;

/**
 * 封装了action拦截器和调用的过程
 */
public final class ActionInvocation {

	private ActionConfig config;

	private Iterator<Interceptor> iterator;
	
	private ActionProxy proxy;
	
	public HttpServletRequest getRequest(){
		return DataFilter.getRequest();
	}
	public HttpServletResponse getResponse(){
		return DataFilter.getResponse();
	}
	
	public void init(ActionConfig config, ActionProxy proxy){
		this.config = config;
		iterator = config.getInterceptors().iterator();
		this.proxy = proxy; 
	}

	public ActionConfig getConfig() {
		return config;
	}
	
	public BaseAction getAction(){
		return proxy.getActionObject();
	}
	
	public Method getActionMethod(){
		return proxy.getMethod();
	}
	
	public String invoke() throws Exception{
		String result = null;
		if(iterator.hasNext()){
			Interceptor interceptor = iterator.next();
			result = interceptor.intercept(this);
		}else{
			result = invokeAction();
		}
		return result;
	}
	
	protected String invokeAction() throws Exception{
		BaseAction action = proxy.getActionObject();
		String methodName = proxy.getMethodName();
		action.processRequest(proxy.getMethod(), methodName);
		return null;
	}
}
