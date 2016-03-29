package com.ocean.exception;

/**
 * sql解析异常
 */
public class SqlParserException extends ShardException {

    private static final long serialVersionUID = -23492934929l;

    public SqlParserException(final String message, final Object... args) {
        super(String.format(message, args));
    }
}
