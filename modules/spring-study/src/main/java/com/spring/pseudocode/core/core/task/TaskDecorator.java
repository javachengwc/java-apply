package com.spring.pseudocode.core.core.task;

public abstract interface TaskDecorator
{
    public abstract Runnable decorate(Runnable paramRunnable);

}