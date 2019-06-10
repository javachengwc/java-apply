package com.pseudocode.cloud.zuul;

public final class ZuulFilterResult {
    private Object result;
    private Throwable exception;
    private ExecutionStatus status;

    public ZuulFilterResult(Object result, ExecutionStatus status) {
        this.result = result;
        this.status = status;
    }

    public ZuulFilterResult(ExecutionStatus status) {
        this.status = status;
    }

    public ZuulFilterResult() {
        this.status = ExecutionStatus.DISABLED;
    }

    public Object getResult() {
        return this.result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public ExecutionStatus getStatus() {
        return this.status;
    }

    public void setStatus(ExecutionStatus status) {
        this.status = status;
    }

    public Throwable getException() {
        return this.exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }
}
