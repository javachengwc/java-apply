package com.spring.pseudocode.web.web.context.request.async;

import com.spring.pseudocode.web.web.context.request.NativeWebRequest;

public abstract interface AsyncWebRequest extends NativeWebRequest
{
    public abstract void setTimeout(Long paramLong);

    public abstract void addTimeoutHandler(Runnable paramRunnable);

    public abstract void addCompletionHandler(Runnable paramRunnable);

    public abstract void startAsync();

    public abstract boolean isAsyncStarted();

    public abstract void dispatch();

    public abstract boolean isAsyncComplete();
}
