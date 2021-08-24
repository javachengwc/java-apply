package com.dubbo.pseudocode.rpc;

import org.apache.dubbo.common.extension.SPI;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;

@SPI
public interface Filter {

    Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException;

    @Deprecated
    default Result onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
        return appResponse;
    }

    interface Listener {

        void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation);

        void onError(Throwable t, Invoker<?> invoker, Invocation invocation);
    }
}
