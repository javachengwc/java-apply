package com.flower.exception;

import java.lang.reflect.Method;

/**
 * 重复action名的异常
 */
public class DuplicateActionException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private Method existedMethod;
	private Method duplicatedMethod;
	private String path;

	public DuplicateActionException(Method existedMethod,
			Method duplicatedMethod, String path) {
		this.existedMethod = existedMethod;
		this.duplicatedMethod = duplicatedMethod;
		this.path = path;
	}
	@Override
	public String getMessage() {
		return "dulicate action for " + path + ":\n" +
				" existedMethod : " + existedMethod.toGenericString() + "\n"
				+ "duplicatedMethod : " + duplicatedMethod.toGenericString();
	}
}
