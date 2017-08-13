package com.app.metrics;

import com.codahale.metrics.MetricRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Controller
@Produces(MediaType.APPLICATION_JSON)
@Path("/app/metrics")
public class MetricsResource {

    @Autowired
    private MetricRegistry metricRegistry;

    @Autowired
    private JvmMetrics jvmMetrics;

    @Autowired
    private GaugeMetrics gaugeMetrics;

    @Autowired
    private TimerMetrics timerMetrics;

    @Autowired
    private MeterMetrics meterMetrics;

    @Autowired
    private CounterMetrics counterMetrics;

    @Autowired
    private HistogramMetrics histogramMetrics;

    @Autowired
    private EmptyMetrics emptyMetrics;

    @GET
    @Path("/metricRegistry")
    public MetricRegistry getmetricRegistry() {
        return metricRegistry;
    }

    @GET
    @Path("/jvm")
    public MetricRegistry getJvmMetrics() {
        return jvmMetrics.getJvmMetrics();
    }

    @GET
    @Path("/gauge")
    public MetricRegistry getGaugeMetrics() {
        return gaugeMetrics.getGaugeMetrics();
    }

    @GET
    @Path("/timer")
    public MetricRegistry getTimerMetrics() {
        return timerMetrics.getTimerMetrics();
    }

    @GET
    @Path("/meter")
    public MetricRegistry getMeterMetrics() {
        return meterMetrics.getMeterMetrics();
    }

    @GET
    @Path("/counter")
    public MetricRegistry getCounterMetrics() {
        return counterMetrics.getCounterMetrics();
    }

    @GET
    @Path("/histogram")
    public MetricRegistry getHistogramMetrics() {
        return histogramMetrics.getHistogramMetrics();
    }

    @GET
    @Path("/empty")
    public MetricRegistry getEmptyMetrics() {
        return emptyMetrics.getEmptyMetrics();
    }

}
