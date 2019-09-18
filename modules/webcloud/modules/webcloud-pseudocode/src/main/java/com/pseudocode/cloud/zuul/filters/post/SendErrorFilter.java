package com.pseudocode.cloud.zuul.filters.post;


import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import com.pseudocode.netflix.zuul.ZuulFilter;
import com.pseudocode.netflix.zuul.context.RequestContext;
import com.pseudocode.netflix.zuul.exception.ZuulException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

//SendErrorFilter：是post阶段第一个执行的过滤器。
//该过滤器仅在请求上下文中包含error.status_code参数（由之前执行的过滤器设置的错误编码）并且还没有被该过滤器处理过的时候执行。
//而该过滤器的具体逻辑就是利用请求上下文中的错误信息来组织成一个forward到API网关/error错误端点的请求来产生错误响应。
public class SendErrorFilter extends ZuulFilter {

    private static final Log log = LogFactory.getLog(SendErrorFilter.class);
    protected static final String SEND_ERROR_FILTER_RAN = "sendErrorFilter.ran";

    @Value("${error.path:/error}")
    private String errorPath;

    @Override
    public String filterType() {
        return ERROR_TYPE;
    }

    @Override
    public int filterOrder() {
        return SEND_ERROR_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        // only forward to errorPath if it hasn't been forwarded to already
        return ctx.getThrowable() != null
                && !ctx.getBoolean(SEND_ERROR_FILTER_RAN, false);
    }

    @Override
    public Object run() {
        try {
            RequestContext ctx = RequestContext.getCurrentContext();
            ExceptionHolder exception = findZuulException(ctx.getThrowable());
            HttpServletRequest request = ctx.getRequest();

            request.setAttribute("javax.servlet.error.status_code", exception.getStatusCode());

            log.warn("Error during filtering", exception.getThrowable());
            request.setAttribute("javax.servlet.error.exception", exception.getThrowable());

            if (StringUtils.hasText(exception.getErrorCause())) {
                request.setAttribute("javax.servlet.error.message", exception.getErrorCause());
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher(
                    this.errorPath);
            if (dispatcher != null) {
                ctx.set(SEND_ERROR_FILTER_RAN, true);
                if (!ctx.getResponse().isCommitted()) {
                    ctx.setResponseStatusCode(exception.getStatusCode());
                    dispatcher.forward(request, ctx.getResponse());
                }
            }
        }
        catch (Exception ex) {
            ReflectionUtils.rethrowRuntimeException(ex);
        }
        return null;
    }

    protected ExceptionHolder findZuulException(Throwable throwable) {
        if (throwable.getCause() instanceof ZuulRuntimeException) {
            // this was a failure initiated by one of the local filters
            return new ZuulExceptionHolder((ZuulException) throwable.getCause().getCause());
        }

        if (throwable.getCause() instanceof ZuulException) {
            // wrapped zuul exception
            return  new ZuulExceptionHolder((ZuulException) throwable.getCause());
        }

        if (throwable instanceof ZuulException) {
            // exception thrown by zuul lifecycle
            return new ZuulExceptionHolder((ZuulException) throwable);
        }

        // fallback
        return new DefaultExceptionHolder(throwable);
    }

    protected interface ExceptionHolder {
        Throwable getThrowable();

        default int getStatusCode() {
            return HttpStatus.INTERNAL_SERVER_ERROR.value();
        }

        default String getErrorCause() {
            return null;
        }
    }

    protected static class DefaultExceptionHolder implements ExceptionHolder {
        private final Throwable throwable;

        public DefaultExceptionHolder(Throwable throwable) {
            this.throwable = throwable;
        }

        @Override
        public Throwable getThrowable() {
            return this.throwable;
        }
    }

    protected static class ZuulExceptionHolder implements ExceptionHolder {
        private final ZuulException exception;

        public ZuulExceptionHolder(ZuulException exception) {
            this.exception = exception;
        }

        @Override
        public Throwable getThrowable() {
            return this.exception;
        }

        @Override
        public int getStatusCode() {
            return this.exception.nStatusCode;
        }

        @Override
        public String getErrorCause() {
            return this.exception.errorCause;
        }
    }

    public void setErrorPath(String errorPath) {
        this.errorPath = errorPath;
    }

}
