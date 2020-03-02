package com.springdubbo.trace.filter;

import com.springdubbo.trace.constant.ConstantTraceLog;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

//Web过滤器，增加logger的traceId
public class WebTraceIDFilter extends OncePerRequestFilter implements Ordered {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        // 读取HTTP头 traceId
        String traceId = request.getHeader(ConstantTraceLog.TRACE_LOG_MDC_KEY);
        if (traceId==null || "".equals(traceId)) {
            traceId = UUID.randomUUID().toString().replace("-", "");
        }

        // 塞到MDC上下文中
        MDC.put(ConstantTraceLog.TRACE_LOG_MDC_KEY, traceId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
