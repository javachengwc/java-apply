package com.ocean.executor;

/**
 * 执行单元
 */
public interface ExecuteUnit<I, O> {

    O execute(I input) throws Exception;
}