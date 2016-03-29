package com.ocean.exception;

/**
 * 不支持数据库类型异常
 */
public class DatabaseTypeUnsupportedException extends RuntimeException {

    private static final long serialVersionUID = -2343546578678534l;

    private static final String MESSAGE = "Can not support database type [%s].";

    public DatabaseTypeUnsupportedException(String databaseType) {
        super(String.format(MESSAGE, databaseType));
    }
}
