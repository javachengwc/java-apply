package com.djy.cluster;

import com.djy.core.Invoker;

import java.util.List;

/**
 * 负载均衡接口
 */
public interface LoadBalance {

    public Invoker select(List<Invoker> invokers);
}
