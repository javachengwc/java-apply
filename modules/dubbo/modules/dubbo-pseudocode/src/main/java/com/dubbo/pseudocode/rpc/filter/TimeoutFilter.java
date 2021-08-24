package com.dubbo.pseudocode.rpc.filter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.rpc.*;

import java.util.Arrays;

@Activate(group = CommonConstants.PROVIDER)
public class TimeoutFilter extends ListenableFilter {

    private static final Logger logger = LoggerFactory.getLogger(TimeoutFilter.class);

    private static final String TIMEOUT_FILTER_START_TIME = "timeout_filter_start_time";

    public TimeoutFilter() {
        super.listener = new TimeoutListener();
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        invocation.setAttachment(TIMEOUT_FILTER_START_TIME, String.valueOf(System.currentTimeMillis()));
        return invoker.invoke(invocation);
    }

    static class TimeoutListener implements Listener {

        @Override
        public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
            String startAttach = invocation.getAttachment(TIMEOUT_FILTER_START_TIME);
            if (startAttach != null) {
                long elapsed = System.currentTimeMillis() - Long.valueOf(startAttach);
                if (invoker.getUrl() != null && elapsed > invoker.getUrl().getMethodParameter(invocation.getMethodName(), "timeout", Integer.MAX_VALUE)) {
                    if (logger.isWarnEnabled()) {
                        logger.warn("invoke time out. method: " + invocation.getMethodName() + " arguments: " + Arrays.toString(invocation.getArguments()) + " , url is " + invoker.getUrl() + ", invoke elapsed " + elapsed + " ms.");
                    }
                }
            }
        }

        @Override
        public void onError(Throwable t, Invoker<?> invoker, Invocation invocation) {

        }
    }
}
