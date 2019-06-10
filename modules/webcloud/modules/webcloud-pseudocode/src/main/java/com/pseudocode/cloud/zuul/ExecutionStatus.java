package com.pseudocode.cloud.zuul;

public enum ExecutionStatus {
    SUCCESS(1),
    SKIPPED(-1),
    DISABLED(-2),
    FAILED(-3);

    private int status;

    private ExecutionStatus(int status) {
        this.status = status;
    }
}
