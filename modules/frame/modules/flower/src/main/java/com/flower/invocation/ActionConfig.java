package com.flower.invocation;

import java.util.ArrayList;
import java.util.List;

import com.flower.interceptor.Interceptor;

/**
 * 封装了Action的配置，目前只有interceptor配置
 */
public class ActionConfig {
	private List<Interceptor> interceptors = new ArrayList<Interceptor>();

	public List<Interceptor> getInterceptors() {
		return interceptors;
	}

	public void setInterceptors(List<Interceptor> interceptors) {
		this.interceptors = interceptors;
	}
}
