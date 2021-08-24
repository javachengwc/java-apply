package com.dubbo.pseudocode.rpc;

import org.apache.dubbo.common.Node;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;

public interface Invoker<T> extends Node {

    Class<T> getInterface();

    Result invoke(Invocation invocation) throws RpcException;

}
