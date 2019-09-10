package com.pseudocode.netflix.zuul;

import com.pseudocode.netflix.zuul.context.RequestContext;
import com.pseudocode.netflix.zuul.exception.ZuulException;
import com.pseudocode.netflix.zuul.http.HttpServletRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class ZuulRunner {
    private boolean bufferRequests;

    public ZuulRunner() {
        this.bufferRequests = true;
    }

    public ZuulRunner(boolean bufferRequests) {
        this.bufferRequests = bufferRequests;
    }

    public void init(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        RequestContext ctx = RequestContext.getCurrentContext();
        if (this.bufferRequests) {
            ctx.setRequest(new HttpServletRequestWrapper(servletRequest));
        } else {
            ctx.setRequest(servletRequest);
        }

        ctx.setResponse(new HttpServletResponseWrapper(servletResponse));
    }

    public void postRoute() throws ZuulException {
        FilterProcessor.getInstance().postRoute();
    }

    public void route() throws ZuulException {
        FilterProcessor.getInstance().route();
    }

    public void preRoute() throws ZuulException {
        FilterProcessor.getInstance().preRoute();
    }

    public void error() {
        FilterProcessor.getInstance().error();
    }

}

