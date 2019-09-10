package com.pseudocode.cloud.zuul.filters.pre;

import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import com.pseudocode.netflix.zuul.ZuulFilter;
import com.pseudocode.netflix.zuul.context.RequestContext;

import javax.servlet.http.HttpServletRequest;

public class DebugFilter extends ZuulFilter
{
    private static final DynamicBooleanProperty ROUTING_DEBUG = DynamicPropertyFactory.getInstance().getBooleanProperty("zuul.debug.request", false);

    private static final DynamicStringProperty DEBUG_PARAMETER = DynamicPropertyFactory.getInstance().getStringProperty("zuul.debug.parameter", "debug");

    public String filterType()
    {
        return "pre";
    }

    public int filterOrder()
    {
        return 1;
    }

    public boolean shouldFilter()
    {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        if ("true".equals(request.getParameter(DEBUG_PARAMETER.get()))) {
            return true;
        }
        return ROUTING_DEBUG.get();
    }

    public Object run()
    {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.setDebugRouting(true);
        ctx.setDebugRequest(true);
        return null;
    }
}
