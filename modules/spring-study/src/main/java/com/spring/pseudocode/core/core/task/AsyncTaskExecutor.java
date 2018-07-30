package com.spring.pseudocode.core.core.task;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public abstract interface AsyncTaskExecutor extends TaskExecutor
{
    public abstract void execute(Runnable runnable, long paramLong);

    public abstract Future<?> submit(Runnable runnable);

    public abstract <T> Future<T> submit(Callable<T> callable);
}
