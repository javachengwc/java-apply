package com.flower;

import java.lang.reflect.Method;

public class ActionWrapper {
	public BaseAction actionObject;
	public Method method;
	public String actionPath;
	public ActionWrapper(BaseAction actionObject, Method method, String actionPath) {
		this.actionObject = actionObject;
		this.method = method;
		this.actionPath = actionPath;
	}
}
