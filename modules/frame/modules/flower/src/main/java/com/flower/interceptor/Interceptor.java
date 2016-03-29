package com.flower.interceptor;

import com.flower.invocation.ActionInvocation;


public interface Interceptor {
	public String intercept(ActionInvocation invocation) throws Exception;
}
