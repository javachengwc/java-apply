package com.pseudocode.netflix.feign.core;

import java.util.Date;

public class RetryableException extends FeignException {

    private static final long serialVersionUID = 1L;

    private final Long retryAfter;

    public RetryableException(String message, Throwable cause, Date retryAfter) {
        super(message, cause);
        this.retryAfter = retryAfter != null ? retryAfter.getTime() : null;
    }
    public RetryableException(String message, Date retryAfter) {
        super(message);
        this.retryAfter = retryAfter != null ? retryAfter.getTime() : null;
    }

    public Date retryAfter() {
        return retryAfter != null ? new Date(retryAfter) : null;
    }
}
