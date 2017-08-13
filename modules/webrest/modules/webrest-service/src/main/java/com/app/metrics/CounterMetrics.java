package com.app.metrics;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CounterMetrics {

    private static Logger logger = LoggerFactory.getLogger(CounterMetrics.class);

    private MetricRegistry metricRegistry = new MetricRegistry();

    private Counter counter=null;

    public CounterMetrics() {
        init();
    }

    public void init() {
        logger.info("CounterMetrics init start.....................");
        //counter是一个计数器,一个AtomicLong实例
        counter = metricRegistry.counter(MetricRegistry.name(CounterMetrics.class, "web-request-count"));
        logger.info("CounterMetrics init end.....................");
    }

    public Counter getCounter() {
        return counter;
    }

    public MetricRegistry getCounterMetrics() {
        return metricRegistry;
    }
}
