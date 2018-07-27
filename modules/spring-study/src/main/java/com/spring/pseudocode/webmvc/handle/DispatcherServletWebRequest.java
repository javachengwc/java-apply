package com.spring.pseudocode.webmvc.handle;

import com.spring.pseudocode.web.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class DispatcherServletWebRequest extends ServletWebRequest
{
    public DispatcherServletWebRequest(HttpServletRequest request)
    {
        super(request);
    }

    public DispatcherServletWebRequest(HttpServletRequest request, HttpServletResponse response)
    {
        super(request, response);
    }

    public Locale getLocale()
    {
        return RequestContextUtils.getLocale(getRequest());
    }
}
