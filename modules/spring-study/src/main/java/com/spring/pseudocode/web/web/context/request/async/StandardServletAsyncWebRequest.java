package com.spring.pseudocode.web.web.context.request.async;

import com.spring.pseudocode.web.web.context.request.ServletWebRequest;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class StandardServletAsyncWebRequest extends ServletWebRequest implements AsyncWebRequest, AsyncListener
{
    private Long timeout;

    private AsyncContext asyncContext;

    private AtomicBoolean asyncCompleted = new AtomicBoolean(false);

    private final List<Runnable> timeoutHandlers = new ArrayList<Runnable>();

    private final List<Runnable> completionHandlers = new ArrayList<Runnable>();

    public StandardServletAsyncWebRequest(HttpServletRequest request, HttpServletResponse response)
    {
        super(request, response);
    }

    public void setTimeout(Long timeout)
    {
        this.timeout = timeout;
    }

    public void addTimeoutHandler(Runnable timeoutHandler)
    {
        this.timeoutHandlers.add(timeoutHandler);
    }

    public void addCompletionHandler(Runnable runnable)
    {
        this.completionHandlers.add(runnable);
    }

    public boolean isAsyncStarted()
    {
        return (this.asyncContext != null) && (getRequest().isAsyncStarted());
    }

    public boolean isAsyncComplete()
    {
        return this.asyncCompleted.get();
    }

    public void startAsync()
    {
        if (isAsyncStarted()) {
            return;
        }
        this.asyncContext = getRequest().startAsync(getRequest(), getResponse());
        this.asyncContext.addListener(this);
        if (this.timeout != null)
            this.asyncContext.setTimeout(this.timeout.longValue());
    }

    public void dispatch()
    {
        this.asyncContext.dispatch();
    }

    public void onStartAsync(AsyncEvent event) throws IOException
    {
    }

    public void onError(AsyncEvent event) throws IOException
    {
        onComplete(event);
    }

    public void onTimeout(AsyncEvent event) throws IOException
    {
        for (Runnable handler : this.timeoutHandlers) {
            handler.run();
        }
    }

    public void onComplete(AsyncEvent event) throws IOException
    {
        for (Runnable handler : this.completionHandlers) {
            handler.run();
        }
        this.asyncContext = null;
        this.asyncCompleted.set(true);
    }
}
