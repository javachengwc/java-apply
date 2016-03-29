package com.ocean.exception;

/**
 * 分片异常
 */
public class ShardException  extends RuntimeException {

    public ShardException(String message, Object... args) {
        super(String.format(message, args));
    }

    public ShardException(Exception cause) {
        super(cause);
    }
}
