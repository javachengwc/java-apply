package com.pseudocode.netflix.ribbon.loadbalancer.server;

import java.util.List;

//服务列表，可以是动态的也可以是静态的，
//如果是动态的（DynamicServerListLoadBalancer），一个后台线程会定时刷新和过滤这个服务列表
public interface ServerList<T extends Server> {

    //获取初始服务列表
    public List<T> getInitialListOfServers();

    //获取最新服务列表
    public List<T> getUpdatedListOfServers();

}
