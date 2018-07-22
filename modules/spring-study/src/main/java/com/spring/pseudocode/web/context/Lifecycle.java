package com.spring.pseudocode.web.context;

public abstract interface Lifecycle
{
    public abstract void start();

    public abstract void stop();

    public abstract boolean isRunning();
}
