package com.flower.exception;


/**
 * 表示重复拦截器ID的异常
 */
public class DuplicateInterceptorIdException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private String id;

	public DuplicateInterceptorIdException(String id) {
		this.id = id;
	}
	@Override
	public String getMessage() {
		return "dulicate interceptor id for " + id;
	}
}
