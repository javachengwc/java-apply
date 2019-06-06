package com.cloud.pseudocode.zuul.exception;

public class ZuulException extends Exception {
    public int nStatusCode;
    public String errorCause;

    public ZuulException(Throwable throwable, String sMessage, int nStatusCode, String errorCause) {
        super(sMessage, throwable);
        this.nStatusCode = nStatusCode;
        this.errorCause = errorCause;
        incrementCounter("ZUUL::EXCEPTION:" + errorCause + ":" + nStatusCode);
    }

    public ZuulException(String sMessage, int nStatusCode, String errorCause) {
        super(sMessage);
        this.nStatusCode = nStatusCode;
        this.errorCause = errorCause;
        incrementCounter("ZUUL::EXCEPTION:" + errorCause + ":" + nStatusCode);

    }

    public ZuulException(Throwable throwable, int nStatusCode, String errorCause) {
        super(throwable.getMessage(), throwable);
        this.nStatusCode = nStatusCode;
        this.errorCause = errorCause;
        incrementCounter("ZUUL::EXCEPTION:" + errorCause + ":" + nStatusCode);

    }

    private static final void incrementCounter(String name) {
        //CounterFactory.instance().increment(name);
    }

}