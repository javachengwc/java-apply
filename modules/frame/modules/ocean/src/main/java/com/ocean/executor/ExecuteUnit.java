package com.ocean.executor;

/**
 * 执行单元
 */
public interface ExecuteUnit<I, O> {

    public O execute(I input) throws Exception;
}