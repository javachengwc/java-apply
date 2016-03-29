package com.flower.interceptor;


public class InterceptorWrapper{
	public Interceptor interceptor;
	public String autoMatch;
	public String excludes;
	public InterceptorWrapper(Interceptor interceptor, String autoMatch, String excludes) {
		this.interceptor = interceptor;
		this.autoMatch = autoMatch;
		this.excludes = excludes;
	}
}
