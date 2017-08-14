package com.app.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class MeterMetrics {

    private static Logger logger = LoggerFactory.getLogger(MeterMetrics.class);

    private MetricRegistry metricRegistry = new MetricRegistry();

    private Meter meter =null;

    public MeterMetrics() {
        init();
    }

    public void init() {
        logger.info("MeterMetrics init start.....................");
        //meters用来度量请求的吞吐,也就是某个时间段的平均处理次数,每1、5、15分钟的tps
//         ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry)
//         .convertRatesTo(TimeUnit.SECONDS).convertDurationsTo(TimeUnit.MILLISECONDS).build();
//         reporter.start(1, TimeUnit.SECONDS);
        meter = metricRegistry.meter(MetricRegistry.name(MeterMetrics.class, "web-request"));
        logger.info("MeterMetrics init end.....................");
    }

    public Meter getMeter() {
        return meter;
    }

    public MetricRegistry getMeterMetrics() {
        return metricRegistry;
    }
}
