package com.spring.pseudocode.web.web.context.request.async;

import com.spring.pseudocode.web.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoSupportAsyncWebRequest extends ServletWebRequest implements AsyncWebRequest
{
    public NoSupportAsyncWebRequest(HttpServletRequest request, HttpServletResponse response)
    {
        super(request, response);
    }

    public void addCompletionHandler(Runnable runnable)
    {
    }

    public void setTimeout(Long timeout)
    {
    }

    public void addTimeoutHandler(Runnable runnable)
    {
    }

    public boolean isAsyncStarted()
    {
        return false;
    }

    public void startAsync()
    {
        throw new UnsupportedOperationException("No async support");
    }

    public boolean isAsyncComplete()
    {
        throw new UnsupportedOperationException("No async support");
    }

    public void dispatch()
    {
        throw new UnsupportedOperationException("No async support");
    }
}
