package com.flower.config;

import org.apache.commons.lang3.StringUtils;

public class InterceptorConfig {
	public InterceptorConfig(String pattern, String className, String excludes, String id){
		if(StringUtils.isBlank(id)){
			throw new InstantiationError("id must be specified for an interceptor, class = " + className);
		}
		this.pattern = pattern;
		this.className = className;
		this.id = id;
	}
	public String pattern;
	public String className;
	public String excludes;
	public String id;
}