package com.shop.book.manage.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RequestReplacedFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(RequestReplacedFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        if (servletRequest instanceof HttpServletRequest) {
            if (!this.isExcludeUrl(((HttpServletRequest) servletRequest))) {
                requestWrapper = new RequestBodyWrapper((HttpServletRequest)servletRequest);
            }
        }
        if (requestWrapper==null) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            filterChain.doFilter(requestWrapper, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }

    //不包含的url
    private boolean isExcludeUrl(HttpServletRequest request) {
        String url = request.getRequestURI();
        if (url.contains("import") || url.contains("upload")) {
            return true;
        }
        return false;
    }


}