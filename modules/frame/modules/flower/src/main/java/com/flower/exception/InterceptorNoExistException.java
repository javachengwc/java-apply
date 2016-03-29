package com.flower.exception;

/**
 * 表示拦截器未被配置或不存在
 */
public class InterceptorNoExistException extends Exception {
	private static final long serialVersionUID = 3205206738164758229L;
	private String id;

	public InterceptorNoExistException(String id) {
		this.id = id;
	}
	
	@Override
	public String getMessage() {
		return "interceptor id [ " + id + "] is not configured!";
	}
}
