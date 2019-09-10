package com.pseudocode.cloud.zuul.filters.pre;


import javax.servlet.http.HttpServletRequest;

import com.pseudocode.netflix.zuul.ZuulFilter;
import com.pseudocode.netflix.zuul.context.RequestContext;
import com.pseudocode.netflix.zuul.http.HttpServletRequestWrapper;
import org.springframework.web.servlet.DispatcherServlet;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.IS_DISPATCHER_SERVLET_REQUEST_KEY;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.PRE_TYPE;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.SERVLET_DETECTION_FILTER_ORDER;

public class ServletDetectionFilter extends ZuulFilter {

    public ServletDetectionFilter() {
    }

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return SERVLET_DETECTION_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        if (!(request instanceof HttpServletRequestWrapper)
                && isDispatcherServletRequest(request)) {
            ctx.set(IS_DISPATCHER_SERVLET_REQUEST_KEY, true);
        } else {
            ctx.set(IS_DISPATCHER_SERVLET_REQUEST_KEY, false);
        }

        return null;
    }

    private boolean isDispatcherServletRequest(HttpServletRequest request) {
        return request.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE) != null;
    }

}
