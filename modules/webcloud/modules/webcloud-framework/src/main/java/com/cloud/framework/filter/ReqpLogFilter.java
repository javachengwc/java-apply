package com.cloud.framework.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Span;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import java.io.IOException;

/**
 * 请求响应日志过滤器
 */
@Component
public class ReqpLogFilter  implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger logger  = LoggerFactory.getLogger("access");

    private final static String REQUEST_START_TIME = "REQUEST_START_TIME";
    private static final String USER_AGENT = "User-Agent";
    private static final String X_FORWARDED_FOR = "x-forwarded-for";
    private static final String REMOTE_ADDR = "remoteAddress";
    private static final String TRACE_ID = "traceId";
    private static final String ACCESS_SEPERATOR = " ";
    private static final String TIME_UNIT = "ms";

    @Context
    private HttpServletRequest request;

    @Value("${spring.application.name}")
    private String appName;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        MDC.put("appName", appName);
        containerRequestContext.setProperty(REQUEST_START_TIME, System.currentTimeMillis());
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        Object startTime = containerRequestContext.getProperty(REQUEST_START_TIME);
        long costTime = 0L;
        if (startTime != null) {
            costTime = System.currentTimeMillis() - (Long) startTime;
        }
        String traceId= request.getHeader(Span.TRACE_ID_NAME);
        String remoteAddr= request.getRemoteAddr();
        String xForwardFor = request.getHeader(X_FORWARDED_FOR);
        String userAgent=containerRequestContext.getHeaderString(USER_AGENT);

        String method=containerRequestContext.getMethod();//GET POST
        Integer status=containerResponseContext.getStatus();
        String path=containerRequestContext.getUriInfo().getRequestUri().getPath();
        String queryStr=containerRequestContext.getUriInfo().getRequestUri().getQuery();

        String logInfo =genLog(traceId,remoteAddr,xForwardFor,userAgent,
                               method,path,status,queryStr,costTime);
        logger.info(logInfo);
    }

    //格式: GET /aa/bb?p1=a&p2=b 200 50ms traceId ....
    private String genLog(String traceId, String remoteAddr, String xForwardFor,  String userAgent,
                          String method,String path, Integer status, String queryStr,long cost) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(method);
        buffer.append(ACCESS_SEPERATOR);

        buffer.append(path);
        if (queryStr != null && queryStr != "") {
            buffer.append("?").append(queryStr);
        }
        buffer.append(ACCESS_SEPERATOR);

        buffer.append(status);
        buffer.append(ACCESS_SEPERATOR);

        buffer.append(""+cost).append(TIME_UNIT);
        buffer.append(ACCESS_SEPERATOR);

        buffer.append(TRACE_ID).append(":").append(traceId);
        buffer.append(ACCESS_SEPERATOR);

        buffer.append(REMOTE_ADDR).append(":").append(remoteAddr);
        buffer.append(ACCESS_SEPERATOR);

        buffer.append(X_FORWARDED_FOR).append(":").append(xForwardFor);
        buffer.append(ACCESS_SEPERATOR);

        buffer.append(USER_AGENT).append(":").append(userAgent);
        return buffer.toString();

    }
}
