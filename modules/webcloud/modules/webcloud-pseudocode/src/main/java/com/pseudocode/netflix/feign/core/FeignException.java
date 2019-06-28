package com.pseudocode.netflix.feign.core;

import java.io.IOException;

import static java.lang.String.format;

public class FeignException extends RuntimeException {

    private static final long serialVersionUID = 0;
    private int status;

    protected FeignException(String message, Throwable cause) {
        super(message, cause);
    }

    protected FeignException(String message) {
        super(message);
    }

    protected FeignException(int status, String message) {
        super(message);
        this.status = status;
    }

    public int status() {
        return this.status;
    }

    static FeignException errorReading(Request request, Response ignored, IOException cause) {
        return new FeignException(format("%s reading %s %s", cause.getMessage(), request.method(), request.url()),cause);
    }

    public static FeignException errorStatus(String methodKey, Response response) {
        String message = format("status %s reading %s", response.status(), methodKey);
        try {
            if (response.body() != null) {
                String body =response.body().asReader().toString();
                message += "; content:\n" + body;
            }
        } catch (IOException ignored) { // NOPMD

        }
        return new FeignException(response.status(), message);
    }

    static FeignException errorExecuting(Request request, IOException cause) {
        return new RetryableException(format("%s executing %s %s", cause.getMessage(), request.method(), request.url()), cause,null);
    }
}
