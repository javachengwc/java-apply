package com.app.metrics;

import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class MetricsReport {

    private static Logger logger= LoggerFactory.getLogger(MetricsReport.class);

    @Autowired
    private JvmMetrics jvmMetrics;

    @Autowired
    private MeterMetrics meterMetrics;

    private Set<GraphiteReporter> reportSet= Collections.synchronizedSet(new HashSet<GraphiteReporter>());

    public MetricsReport() {
    }

    public void start() {

        logger.info("MetricsReport report start start.............");
        Graphite graphite = new Graphite("0.0.0.0", 2013);//carbon-relay地址端口
        GraphiteReporter jvmReporter = GraphiteReporter.forRegistry(jvmMetrics.getJvmMetrics())
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .filter(MetricFilter.ALL)
                .prefixedWith("reporter.jvm")
                .build(graphite);
        jvmReporter.start(1, TimeUnit.SECONDS);
        reportSet.add(jvmReporter);

        GraphiteReporter reporter = GraphiteReporter.forRegistry(meterMetrics.getMeterMetrics())
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .filter(MetricFilter.ALL)
                .prefixedWith("reporter.meter")
                .build(graphite);
        reporter.start(1, TimeUnit.SECONDS);
        reportSet.add(reporter);

        logger.info("MetricsReport report start end.............");
    }

    public void stop() {
        logger.info("MetricsReport report stop.............");
        for(GraphiteReporter reporter:reportSet) {
            reporter.stop();
        }
    }
}
