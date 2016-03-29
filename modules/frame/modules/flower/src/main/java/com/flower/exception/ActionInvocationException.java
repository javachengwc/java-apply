package com.flower.exception;

public class ActionInvocationException extends Exception {
	private static final long serialVersionUID = 2142453654677L;

	public ActionInvocationException() {
		super();
	}

	public ActionInvocationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ActionInvocationException(String message) {
		super(message);
	}

	public ActionInvocationException(Throwable cause) {
		super(cause);
	}
	
}
