package com.app.health;

import com.codahale.metrics.health.HealthCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

public class AppHealthCheck extends HealthCheck {

    private static Logger logger = LoggerFactory.getLogger(AppHealthCheck.class);

    private boolean isHealth=true;

    private Timer timer=new Timer("HealthCheckTimer");

    public AppHealthCheck() {
        init();
    }

    public void init() {
        logger.info("AppHealthCheck init start.....................");

        //周期改变健康标记
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                isHealth=!isHealth;
                logger.info("AppHealthCheck TimerTask run,cur isHealth="+isHealth);
            }
        },1000,5000);

        logger.info("AppHealthCheck init end.....................");
    }

    @Override
    public HealthCheck.Result check() throws Exception {
        if (isHealth) {
            return HealthCheck.Result.healthy();
        } else {
            return HealthCheck.Result.unhealthy("app is un health");
        }
    }
}
