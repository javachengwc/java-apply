package com.spring.pseudocode.core.core.task;

import java.util.concurrent.Executor;

public abstract interface TaskExecutor extends Executor
{
    public abstract void execute(Runnable runnable);
}
