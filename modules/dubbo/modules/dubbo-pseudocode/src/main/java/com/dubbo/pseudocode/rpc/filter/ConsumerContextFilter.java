package com.dubbo.pseudocode.rpc.filter;

import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.NetUtils;
import org.apache.dubbo.rpc.*;

import static org.apache.dubbo.common.constants.CommonConstants.CONSUMER;

@Activate(group = CONSUMER, order = -10000)
public class ConsumerContextFilter extends ListenableFilter {

    public ConsumerContextFilter() {
        super.listener = new ConsumerContextListener();
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext.getContext()
                .setInvoker(invoker)
                .setInvocation(invocation)
                .setLocalAddress(NetUtils.getLocalHost(), 0)
                .setRemoteAddress(invoker.getUrl().getHost(),
                        invoker.getUrl().getPort());
        if (invocation instanceof RpcInvocation) {
            ((RpcInvocation) invocation).setInvoker(invoker);
        }
        try {
            RpcContext.removeServerContext();
            return invoker.invoke(invocation);
        } finally {
            RpcContext.removeContext();
        }
    }

    static class ConsumerContextListener implements Listener {
        @Override
        public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
            RpcContext.getServerContext().setAttachments(appResponse.getAttachments());
        }

        @Override
        public void onError(Throwable t, Invoker<?> invoker, Invocation invocation) {

        }
    }
}
