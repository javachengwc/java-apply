package com.dubbo.pseudocode.rpc;

import org.apache.dubbo.rpc.Invoker;

public interface Exporter<T> {

    Invoker<T> getInvoker();

    void unexport();

}
