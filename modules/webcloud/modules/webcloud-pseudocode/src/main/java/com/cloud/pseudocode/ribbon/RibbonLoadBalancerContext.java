package com.cloud.pseudocode.ribbon;

import com.cloud.pseudocode.ribbon.core.client.RetryHandler;
import com.cloud.pseudocode.ribbon.core.client.config.IClientConfig;
import com.cloud.pseudocode.ribbon.loadbalancer.ILoadBalancer;
import com.cloud.pseudocode.ribbon.loadbalancer.LoadBalancerContext;
import com.cloud.pseudocode.ribbon.loadbalancer.ServerStats;
import com.netflix.servo.monitor.Timer;


public class RibbonLoadBalancerContext extends LoadBalancerContext {

    public RibbonLoadBalancerContext(ILoadBalancer lb) {
        super(lb);
    }

    public RibbonLoadBalancerContext(ILoadBalancer lb, IClientConfig clientConfig) {
        super(lb, clientConfig);
    }

    public RibbonLoadBalancerContext(ILoadBalancer lb, IClientConfig clientConfig, RetryHandler handler) {
        super(lb, clientConfig, handler);
    }

    @Override
    public void noteOpenConnection(ServerStats serverStats) {
        super.noteOpenConnection(serverStats);
    }

    @Override
    public Timer getExecuteTracer() {
        return super.getExecuteTracer();
    }

    @Override
    public void noteRequestCompletion(ServerStats stats, Object response, Throwable e,
                                      long responseTime) {
        super.noteRequestCompletion(stats, response, e, responseTime);
    }

    @Override
    public void noteRequestCompletion(ServerStats stats, Object response, Throwable e,
                                      long responseTime, RetryHandler errorHandler) {
        super.noteRequestCompletion(stats, response, e, responseTime, errorHandler);
    }

}
