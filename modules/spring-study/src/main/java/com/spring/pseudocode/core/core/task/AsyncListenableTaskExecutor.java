package com.spring.pseudocode.core.core.task;

import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;

public abstract interface AsyncListenableTaskExecutor extends AsyncTaskExecutor
{
    public abstract ListenableFuture<?> submitListenable(Runnable paramRunnable);

    public abstract <T> ListenableFuture<T> submitListenable(Callable<T> paramCallable);
}