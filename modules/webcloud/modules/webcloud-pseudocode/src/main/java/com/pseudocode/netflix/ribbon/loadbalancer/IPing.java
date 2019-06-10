package com.pseudocode.netflix.ribbon.loadbalancer;

import com.pseudocode.netflix.ribbon.loadbalancer.server.Server;

//一个后台运行的组件，确定服务是否可用
public interface IPing {

    public boolean isAlive(Server server);
}
