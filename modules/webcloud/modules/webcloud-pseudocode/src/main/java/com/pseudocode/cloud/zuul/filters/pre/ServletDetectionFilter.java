package com.pseudocode.cloud.zuul.filters.pre;


import javax.servlet.http.HttpServletRequest;

import com.pseudocode.netflix.zuul.ZuulFilter;
import com.pseudocode.netflix.zuul.context.RequestContext;
import com.pseudocode.netflix.zuul.http.HttpServletRequestWrapper;
import org.springframework.web.servlet.DispatcherServlet;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.IS_DISPATCHER_SERVLET_REQUEST_KEY;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.PRE_TYPE;
import static com.pseudocode.cloud.zuul.filters.support.FilterConstants.SERVLET_DETECTION_FILTER_ORDER;

//检测当前情况是否通过spring的DispatcherServlet处理运行
//一般情况下，发送到API网关的外部请求都会被Spring的DispatcherServlet处理，
//除了通过/zuul/路径访问的请求会绕过DispatcherServlet，被ZuulServlet处理，主要用来应对处理大文件上传的情况。
//另外，对于ZuulServlet的访问路径/zuul/，可以通过zuul.servletPath参数来进行修改。
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
