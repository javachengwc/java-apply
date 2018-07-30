package com.spring.pseudocode.web.web.context.request.async;

import com.spring.pseudocode.web.web.context.request.WebRequest;
import org.springframework.util.ClassUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class WebAsyncUtils
{
    public static final String WEB_ASYNC_MANAGER_ATTRIBUTE = WebAsyncManager.class.getName() + ".WEB_ASYNC_MANAGER";

    private static final boolean startAsyncAvailable = ClassUtils.hasMethod(ServletRequest.class, "startAsync", new Class[0]);

    public static WebAsyncManager getAsyncManager(ServletRequest servletRequest)
    {
        WebAsyncManager asyncManager = (WebAsyncManager)servletRequest.getAttribute(WEB_ASYNC_MANAGER_ATTRIBUTE);
        if (asyncManager == null) {
            asyncManager = new WebAsyncManager();
            servletRequest.setAttribute(WEB_ASYNC_MANAGER_ATTRIBUTE, asyncManager);
        }
        return asyncManager;
    }

    public static WebAsyncManager getAsyncManager(WebRequest webRequest)
    {
        int scope = 0;
        WebAsyncManager asyncManager = (WebAsyncManager)webRequest.getAttribute(WEB_ASYNC_MANAGER_ATTRIBUTE, scope);
        if (asyncManager == null) {
            asyncManager = new WebAsyncManager();
            webRequest.setAttribute(WEB_ASYNC_MANAGER_ATTRIBUTE, asyncManager, scope);
        }
        return asyncManager;
    }

    public static AsyncWebRequest createAsyncWebRequest(HttpServletRequest request, HttpServletResponse response)
    {
        return startAsyncAvailable ? AsyncWebRequestFactory.createStandardAsyncWebRequest(request, response) : new NoSupportAsyncWebRequest(request, response);
    }

    private static class AsyncWebRequestFactory
    {
        public static AsyncWebRequest createStandardAsyncWebRequest(HttpServletRequest request, HttpServletResponse response)
        {
            return new StandardServletAsyncWebRequest(request, response);
        }
    }
}