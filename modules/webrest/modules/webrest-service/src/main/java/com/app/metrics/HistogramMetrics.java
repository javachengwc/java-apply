package com.app.metrics;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HistogramMetrics {

    private static Logger logger = LoggerFactory.getLogger(HistogramMetrics.class);

    private MetricRegistry metricRegistry = new MetricRegistry();

    private Histogram histogram =null;

    public HistogramMetrics() {
        init();
    }

    public void init() {
        logger.info("HistogramMetrics init start.....................");
        //histogram(直方图),为数据提供统计数据,最大值、最小值、平均值、中位数，百分比（75%、90%、95%等）
        histogram = metricRegistry.histogram(MetricRegistry.name(HistogramMetrics.class, "random"));
        logger.info("HistogramMetrics init end.....................");
    }

    public Histogram getHistogram() {
        return histogram;
    }

    public MetricRegistry getHistogramMetrics() {
        return metricRegistry;
    }
}
