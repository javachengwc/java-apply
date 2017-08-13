package com.app.health;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class HealthCheckManager {

    private static Logger logger = LoggerFactory.getLogger(HealthCheckManager.class);

    private static HealthCheckManager manager = new HealthCheckManager();

    public static HealthCheckManager getInstance() {
        return manager;
    }

    private HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();

    private HealthCheckManager() {
        init();
    }

    public void init() {
        logger.info("HealthCheckManager init start.................");
        register("app",new AppHealthCheck());
    }

    public void register(String name,HealthCheck healthCheck) {
        logger.info("HealthCheckManager register name={},healthCheck={}",name,healthCheck.getClass().getName());
        healthCheckRegistry.register(name,healthCheck);
    }

    public Object run() {
        logger.info("HealthCheckManager check health start..........");
        Map<String, HealthCheck.Result> results = healthCheckRegistry.runHealthChecks();
        return results;
    }
}
