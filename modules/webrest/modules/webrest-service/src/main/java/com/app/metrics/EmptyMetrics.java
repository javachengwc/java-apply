package com.app.metrics;

import com.codahale.metrics.MetricRegistry;
import org.springframework.stereotype.Component;

@Component
public class EmptyMetrics {

    private MetricRegistry metricRegistry = new MetricRegistry();

    public MetricRegistry getEmptyMetrics() {
        return metricRegistry;
    }
}
