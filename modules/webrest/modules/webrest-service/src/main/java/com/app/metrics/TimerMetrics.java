package com.app.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TimerMetrics {

    private static Logger logger = LoggerFactory.getLogger(TimerMetrics.class);

    private MetricRegistry metricRegistry = new MetricRegistry();

    private Timer requestTimer=null;

    public TimerMetrics() {
        init();
    }

    public void init() {
        logger.info("TimerMetrics init start.....................");
        //timer(计时器),用来测量一段代码被调用的速率和用时。
        requestTimer = metricRegistry.timer(MetricRegistry.name(TimerMetrics.class, "web-request"));
        logger.info("TimerMetrics init end.....................");
    }

    public Timer getRequestTimer() {
        return requestTimer;
    }

    public MetricRegistry getTimerMetrics() {
        return metricRegistry;
    }
}
