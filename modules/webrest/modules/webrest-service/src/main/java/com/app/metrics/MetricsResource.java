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
}
