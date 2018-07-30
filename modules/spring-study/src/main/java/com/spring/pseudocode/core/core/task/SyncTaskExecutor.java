package com.spring.pseudocode.core.core.task;

import java.io.Serializable;

public class SyncTaskExecutor implements TaskExecutor, Serializable
{
    public void execute(Runnable task)
    {
        task.run();
    }
}