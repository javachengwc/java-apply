package com.flower.invocation;

import java.lang.reflect.Method;

import com.flower.BaseAction;

/**
 * Action代理，本身不做action调用的工作，只是调用Invocation本身
 */
public class ActionProxy extends BaseAction {

	private BaseAction actionObject;
	
	private ActionInvocation invocation;
	
	private String methodName;

	private Method method;
	
	public void execute() throws Exception{
		invocation.invoke();
	}
	
	
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public BaseAction getActionObject() {
		return actionObject;
	}

	public void setActionObject(BaseAction actionObject) {
		this.actionObject = actionObject;
	}

	public ActionInvocation getInvocation() {
		return invocation;
	}

	public void setInvocation(ActionInvocation invocation) {
		this.invocation = invocation;
	}


	public void setMethod(Method method) {
		this.method = method;
	}
	
	public Method getMethod() {
		return method;
	}
}
