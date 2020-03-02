package com.springdubbo.trace.filter;

import com.springdubbo.trace.constant.ConstantTraceLog;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;
import org.slf4j.MDC;

@Slf4j
@Activate(group = {CommonConstants.PROVIDER})
public class DubboProviderTraceIDFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation inv) throws RpcException {
        setTraceId(inv);
        try {
            return invoker.invoke(inv);
        } finally {
            MDC.clear();
        }
    }

    private void setTraceId(Invocation inv) {
        String traceId = inv.getAttachment(ConstantTraceLog.TRACE_ID);
        if(traceId==null || "".equals(traceId)){
            traceId = UUID.randomUUID().toString().replaceAll("-", "");
        }
        MDC.put(ConstantTraceLog.TRACE_LOG_MDC_KEY, traceId);
    }
}