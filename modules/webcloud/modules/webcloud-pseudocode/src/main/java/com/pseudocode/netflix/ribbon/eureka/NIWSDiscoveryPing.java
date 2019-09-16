package com.pseudocode.netflix.ribbon.eureka;

import com.pseudocode.netflix.eureka.client.appinfo.InstanceInfo;
import com.pseudocode.netflix.eureka.client.appinfo.InstanceInfo.InstanceStatus;
import com.pseudocode.netflix.ribbon.core.client.config.IClientConfig;
import com.pseudocode.netflix.ribbon.loadbalancer.AbstractLoadBalancerPing;
import com.pseudocode.netflix.ribbon.loadbalancer.BaseLoadBalancer;
import com.pseudocode.netflix.ribbon.loadbalancer.server.Server;

public class NIWSDiscoveryPing extends AbstractLoadBalancerPing {

    BaseLoadBalancer lb = null;


    public NIWSDiscoveryPing() {
    }

    public BaseLoadBalancer getLb() {
        return lb;
    }

    public void setLb(BaseLoadBalancer lb) {
        this.lb = lb;
    }

    //根据DiscoveryEnabledServer的InstanceInfo的InstanceStatus去判断，如果为InstanceStatus.UP，则为可用，否则不可用
    public boolean isAlive(Server server) {
        boolean isAlive = true;
        if (server!=null && server instanceof DiscoveryEnabledServer){
            DiscoveryEnabledServer dServer = (DiscoveryEnabledServer)server;
            InstanceInfo instanceInfo = dServer.getInstanceInfo();
            if (instanceInfo!=null){
                InstanceStatus status = instanceInfo.getStatus();
                if (status!=null){
                    isAlive = status.equals(InstanceStatus.UP);
                }
            }
        }
        return isAlive;
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
    }

}

