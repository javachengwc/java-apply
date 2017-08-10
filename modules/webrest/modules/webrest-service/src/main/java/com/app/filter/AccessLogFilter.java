package com.app.filter;

import com.app.entity.AccessLog;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import java.io.IOException;

@Component
public class AccessLogFilter  implements ContainerRequestFilter, ContainerResponseFilter {

    private static Logger logger = LoggerFactory.getLogger(AccessLogFilter.class);

    private static String X_FORWARDED_FOR = "x-forwarded-for";

    private static String costTimeKey="cost";

    private static String statusKey="status";

    private static String beginTimeKey="beginTime";

    private static String remoteAddrKey="remoteAddr";

    private static String traceIdKey = "traceId";

    private Gson gson = new Gson();

    @Context
    private HttpServletRequest servletRequest;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        containerRequestContext.setProperty(beginTimeKey, System.currentTimeMillis());
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        Object beginTimeObj = containerRequestContext.getProperty(beginTimeKey);
        long costTime = 0L;
        if (beginTimeObj != null) {
            costTime = System.currentTimeMillis() - (Long)beginTimeObj;
        }
        String curTrace=servletRequest.getHeader(traceIdKey);
        String remoteAddr=servletRequest.getRemoteAddr();
        String xFwdFor=servletRequest.getHeader(X_FORWARDED_FOR);
        String methodType=containerRequestContext.getMethod();
        String path=containerRequestContext.getUriInfo().getRequestUri().getPath();
        String queryStr= containerRequestContext.getUriInfo().getRequestUri().getQuery();
        Integer status=containerResponseContext.getStatus();
        AccessLog accessLog= new AccessLog(methodType,path,queryStr,status,costTime,curTrace,remoteAddr,xFwdFor);
        String logStr =genAccessLog(accessLog);
        logger.info(logStr);
    }

    private String genAccessLog(AccessLog accessLog) {
        StringBuffer buf = new StringBuffer("AccessLog------>");
        buf.append(accessLog.getMethodType()).append(" ").append(accessLog.getPath());
        String queryValue=accessLog.getQueryValue();
        if (!StringUtils.isBlank(queryValue)) {
            buf.append("?").append(queryValue);
        }
        buf.append(" ");
        buf.append("status:").append(accessLog.getStatus()).append(" ");
        buf.append("cost:").append(accessLog.getCostTime()).append("ms");
        buf.append(" ").append(gson.toJson(accessLog));
        return buf.toString();
    }
}
