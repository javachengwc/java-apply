package com.cloud.pseudocode.ribbon;

import com.cloud.pseudocode.ribbon.loadbalancer.Server;
import com.cloud.pseudocode.ribbon.loadbalancer.ServerStats;
import com.netflix.servo.monitor.Stopwatch;

import java.util.concurrent.TimeUnit;

public class RibbonStatsRecorder {

    private RibbonLoadBalancerContext context;
    private ServerStats serverStats;
    private Stopwatch tracer;

    public RibbonStatsRecorder(RibbonLoadBalancerContext context, Server server) {
        this.context = context;
        if (server != null) {
            serverStats = context.getServerStats(server);
            context.noteOpenConnection(serverStats);
            tracer = context.getExecuteTracer().start();
        }
    }

    public void recordStats(Object entity) {
        this.recordStats(entity, null);
    }

    public void recordStats(Throwable t) {
        this.recordStats(null, t);
    }

    protected void recordStats(Object entity, Throwable exception) {
        if (this.tracer != null && this.serverStats != null) {
            this.tracer.stop();
            long duration = this.tracer.getDuration(TimeUnit.MILLISECONDS);
            this.context.noteRequestCompletion(serverStats, entity, exception, duration, null/* errorHandler */);
        }
    }
}

