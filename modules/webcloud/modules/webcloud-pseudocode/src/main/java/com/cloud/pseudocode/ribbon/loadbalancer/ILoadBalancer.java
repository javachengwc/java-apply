package com.cloud.pseudocode.ribbon.loadbalancer;

import java.util.List;

public interface ILoadBalancer {

    //向负载均衡器中维护的实例列表增加服务实例
    public void addServers(List<Server> newServers);

    //从负载均衡器中挑选出一个具体的服务实例
    public Server chooseServer(Object key);

    //用来通知和标记负载均衡器中的某个具体实例已经停止服务，不然负载均衡器在下一次获取服务实例清单前都会认为服务实例均是正常服务的。
    public void markServerDown(Server server);

    @Deprecated
    public List<Server> getServerList(boolean availableOnly);

    //获取当前正常服务的实例列表
    public List<Server> getReachableServers();

    //获取所有已知的服务实例列表，包括正常服务和停止服务实例。
    public List<Server> getAllServers();
}
