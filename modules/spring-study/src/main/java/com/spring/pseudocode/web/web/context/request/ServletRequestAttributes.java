package com.spring.pseudocode.web.web.context.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServletRequestAttributes implements RequestAttributes
{

    private final HttpServletRequest request;
    private HttpServletResponse response;
    private volatile HttpSession session;
    private final Map<String, Object> sessionAttributesToUpdate = new ConcurrentHashMap(1);
    private volatile boolean requestActive = true;

    public ServletRequestAttributes(HttpServletRequest request)
    {
        this.request = request;
    }

    public ServletRequestAttributes(HttpServletRequest request, HttpServletResponse response)
    {
        this(request);
        this.response = response;
    }

    public final HttpServletRequest getRequest()
    {
        return this.request;
    }

    public final HttpServletResponse getResponse()
    {
        return this.response;
    }

    protected final boolean isRequestActive()
    {
        return this.requestActive;
    }

    public void requestCompleted()
    {
        this.requestActive = false;
    }

    protected final HttpSession getSession(boolean allowCreate)
    {
        if (isRequestActive()) {
            return this.request.getSession(allowCreate);
        }

        return this.session;
    }

    public Object getAttribute(String name, int scope)
    {
        if (scope == 0) {
            return this.request.getAttribute(name);
        }

        HttpSession session = getSession(false);
        if (session != null) {
            try {
                Object value = session.getAttribute(name);
                if (value != null) {
                    this.sessionAttributesToUpdate.put(name, value);
                }
                return value;
            }
            catch (IllegalStateException localIllegalStateException)
            {
            }
        }
        return null;
    }

    public void setAttribute(String name, Object value, int scope)
    {
        if (scope == 0) {
            this.request.setAttribute(name, value);
        }
        else {
            HttpSession session = getSession(true);
            this.sessionAttributesToUpdate.remove(name);
            session.setAttribute(name, value);
        }
    }

    public String getSessionId()
    {
        return getSession(true).getId();
    }

}
