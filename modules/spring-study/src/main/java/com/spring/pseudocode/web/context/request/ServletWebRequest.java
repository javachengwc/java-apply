package com.spring.pseudocode.web.context.request;


import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class ServletWebRequest extends ServletRequestAttributes implements WebRequest
{
    private static final String HEADER_ETAG = "ETag";
    private static final String HEADER_IF_MODIFIED_SINCE = "If-Modified-Since";
    private static final String HEADER_IF_NONE_MATCH = "If-None-Match";
    private static final String HEADER_LAST_MODIFIED = "Last-Modified";
    private static final String METHOD_GET = "GET";
    private static final String METHOD_HEAD = "HEAD";

    private boolean notModified = false;

    public ServletWebRequest(HttpServletRequest request)
    {
        super(request);
    }

    public ServletWebRequest(HttpServletRequest request, HttpServletResponse response)
    {
        super(request, response);
    }

    public Object getNativeRequest()
    {
        return getRequest();
    }

    public Object getNativeResponse()
    {
        return getResponse();
    }

    public HttpMethod getHttpMethod()
    {
        return HttpMethod.resolve(getRequest().getMethod());
    }

    public String getHeader(String headerName)
    {
        return getRequest().getHeader(headerName);
    }


    public String getParameter(String paramName)
    {
        return getRequest().getParameter(paramName);
    }

    public String[] getParameterValues(String paramName)
    {
        return getRequest().getParameterValues(paramName);
    }

    public Iterator<String> getParameterNames()
    {
        return CollectionUtils.toIterator(getRequest().getParameterNames());
    }

    public Map<String, String[]> getParameterMap()
    {
        return getRequest().getParameterMap();
    }

    public Locale getLocale()
    {
        return getRequest().getLocale();
    }

    public String getContextPath()
    {
        return getRequest().getContextPath();
    }

    public String getRemoteUser()
    {
        return getRequest().getRemoteUser();
    }

    public boolean isSecure()
    {
        return getRequest().isSecure();
    }

}
