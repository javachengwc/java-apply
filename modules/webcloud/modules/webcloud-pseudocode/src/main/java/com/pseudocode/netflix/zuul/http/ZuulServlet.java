package com.pseudocode.netflix.zuul.http;

import com.pseudocode.netflix.zuul.ZuulRunner;
import com.pseudocode.netflix.zuul.context.RequestContext;
import com.pseudocode.netflix.zuul.exception.ZuulException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ZuulServlet extends HttpServlet {

    private static final long serialVersionUID = -3374242278843351500L;
    private ZuulRunner zuulRunner;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        String bufferReqsStr = config.getInitParameter("buffer-requests");
        boolean bufferReqs = bufferReqsStr != null && bufferReqsStr.equals("true") ? true : false;

        zuulRunner = new ZuulRunner(bufferReqs);
    }

    @Override
    public void service(javax.servlet.ServletRequest servletRequest, javax.servlet.ServletResponse servletResponse) throws ServletException, IOException {
        try {
            init((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);

            RequestContext context = RequestContext.getCurrentContext();
            context.setZuulEngineRan();

            try {
                preRoute();
            } catch (ZuulException e) {
                error(e);
                postRoute();
                return;
            }
            try {
                route();
            } catch (ZuulException e) {
                error(e);
                postRoute();
                return;
            }
            try {
                postRoute();
            } catch (ZuulException e) {
                error(e);
                return;
            }

        } catch (Throwable e) {
            error(new ZuulException(e, 500, "UNHANDLED_EXCEPTION_" + e.getClass().getName()));
        } finally {
            RequestContext.getCurrentContext().unset();
        }
    }

    /**
     * executes "post" ZuulFilters
     */
    void postRoute() throws ZuulException {
        zuulRunner.postRoute();
    }

    /**
     * executes "route" filters
     */
    void route() throws ZuulException {
        zuulRunner.route();
    }

    /**
     * executes "pre" filters
     */
    void preRoute() throws ZuulException {
        zuulRunner.preRoute();
    }

    /**
     * initializes request
     */
    void init(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        zuulRunner.init(servletRequest, servletResponse);
    }

    /**
     * sets error context info and executes "error" filters
     */
    void error(ZuulException e) {
        RequestContext.getCurrentContext().setThrowable(e);
        zuulRunner.error();
    }

}
