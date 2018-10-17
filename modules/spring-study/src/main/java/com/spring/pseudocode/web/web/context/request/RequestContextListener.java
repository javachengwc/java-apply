package com.spring.pseudocode.web.web.context.request;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

public class RequestContextListener implements ServletRequestListener
{
    private static final String REQUEST_ATTRIBUTES_ATTRIBUTE = RequestContextListener.class.getName() + ".REQUEST_ATTRIBUTES";

    public void requestInitialized(ServletRequestEvent requestEvent)
    {
        if (!(requestEvent.getServletRequest() instanceof HttpServletRequest))
        {
            throw new IllegalArgumentException("Request is not an HttpServletRequest: " + requestEvent.getServletRequest());
        }
        HttpServletRequest request = (HttpServletRequest)requestEvent.getServletRequest();
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        request.setAttribute(REQUEST_ATTRIBUTES_ATTRIBUTE, attributes);
        //LocaleContextHolder.setLocale(request.getLocale());
        RequestContextHolder.setRequestAttributes(attributes);
    }

    public void requestDestroyed(ServletRequestEvent requestEvent)
    {
        ServletRequestAttributes attributes = null;
        Object reqAttr = requestEvent.getServletRequest().getAttribute(REQUEST_ATTRIBUTES_ATTRIBUTE);
        if ((reqAttr instanceof ServletRequestAttributes)) {
            attributes = (ServletRequestAttributes)reqAttr;
        }
        RequestAttributes threadAttributes = RequestContextHolder.getRequestAttributes();
        if (threadAttributes != null)
        {
            //LocaleContextHolder.resetLocaleContext();
            RequestContextHolder.resetRequestAttributes();
            if ((attributes == null) && ((threadAttributes instanceof ServletRequestAttributes))) {
                attributes = (ServletRequestAttributes)threadAttributes;
            }
        }
        if (attributes != null)
            attributes.requestCompleted();
    }
}
