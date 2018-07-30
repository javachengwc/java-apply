package com.spring.pseudocode.core.core.task;


import org.springframework.util.ConcurrencyThrottleSupport;
import org.springframework.util.CustomizableThreadCreator;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureTask;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;

public class SimpleAsyncTaskExecutor extends CustomizableThreadCreator implements AsyncListenableTaskExecutor, Serializable
{
    public static final int UNBOUNDED_CONCURRENCY = -1;

    public static final int NO_CONCURRENCY = 0;

    private final ConcurrencyThrottleAdapter concurrencyThrottle = new ConcurrencyThrottleAdapter();

    private ThreadFactory threadFactory;

    private TaskDecorator taskDecorator;

    public SimpleAsyncTaskExecutor()
    {
    }

    public SimpleAsyncTaskExecutor(String threadNamePrefix)
    {
        super(threadNamePrefix);
    }

    public SimpleAsyncTaskExecutor(ThreadFactory threadFactory)
    {
        this.threadFactory = threadFactory;
    }

    public void setThreadFactory(ThreadFactory threadFactory)
    {
        this.threadFactory = threadFactory;
    }

    public final ThreadFactory getThreadFactory()
    {
        return this.threadFactory;
    }

    public final void setTaskDecorator(TaskDecorator taskDecorator)
    {
        this.taskDecorator = taskDecorator;
    }

    public void setConcurrencyLimit(int concurrencyLimit)
    {
        this.concurrencyThrottle.setConcurrencyLimit(concurrencyLimit);
    }

    public final int getConcurrencyLimit()
    {
        return this.concurrencyThrottle.getConcurrencyLimit();
    }

    public final boolean isThrottleActive()
    {
        return this.concurrencyThrottle.isThrottleActive();
    }

    public void execute(Runnable task)
    {
        execute(task, 9223372036854775807L);
    }

    public void execute(Runnable task, long startTimeout)
    {
        Runnable taskToUse = this.taskDecorator != null ? this.taskDecorator.decorate(task) : task;
        if ((isThrottleActive()) && (startTimeout > 0L)) {
            this.concurrencyThrottle.beforeAccess();
            doExecute(new ConcurrencyThrottlingRunnable(taskToUse));
        }
        else {
            doExecute(taskToUse);
        }
    }

    public Future<?> submit(Runnable task)
    {
        FutureTask future = new FutureTask(task, null);
        execute(future, Long.MAX_VALUE);
        return future;
    }

    public <T> Future<T> submit(Callable<T> task)
    {
        FutureTask future = new FutureTask(task);
        execute(future, Long.MAX_VALUE);
        return future;
    }

    public ListenableFuture<?> submitListenable(Runnable task)
    {
        ListenableFutureTask future = new ListenableFutureTask(task, null);
        execute(future, Long.MAX_VALUE);
        return future;
    }

    public <T> ListenableFuture<T> submitListenable(Callable<T> task)
    {
        ListenableFutureTask future = new ListenableFutureTask(task);
        execute(future, Long.MAX_VALUE);
        return future;
    }

    protected void doExecute(Runnable task)
    {
        Thread thread = this.threadFactory != null ? this.threadFactory.newThread(task) : createThread(task);
        thread.start();
    }

    private class ConcurrencyThrottlingRunnable implements Runnable
    {
        private final Runnable target;

        public ConcurrencyThrottlingRunnable(Runnable target)
        {
            this.target = target;
        }

        public void run()
        {
            try {
                this.target.run();
                SimpleAsyncTaskExecutor.this.concurrencyThrottle.afterAccess();
            } finally {
                SimpleAsyncTaskExecutor.this.concurrencyThrottle.afterAccess();
            }
        }
    }

    private static class ConcurrencyThrottleAdapter extends ConcurrencyThrottleSupport
    {
        protected void beforeAccess()
        {
            super.beforeAccess();
        }

        protected void afterAccess()
        {
            super.afterAccess();
        }
    }
}
