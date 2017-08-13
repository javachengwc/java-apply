package com.app.metrics;

import com.app.service.ServiceFactory;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GaugeMetrics {

    private static Logger logger = LoggerFactory.getLogger(GaugeMetrics.class);

    private MetricRegistry metricRegistry = new MetricRegistry();

    public GaugeMetrics() {
        init();
    }

    public void init() {
        logger.info("GaugeMetrics init start.....................");
        //gauge(仪表),表示一个度量的即时值。
        //当测温度时,当前温度就是一个gauge值,在运行程序中,当前服务bean调用次数也是一个gauge值
        Gauge<Long> serviceGauge = new Gauge<Long>() {
            public Long getValue() {
                return ServiceFactory.getServiceIncokeCount();
            }
        };
        //注册到容器中
        metricRegistry.register(MetricRegistry.name(GaugeMetrics.class,"invokeCount"), serviceGauge);
        logger.info("GaugeMetrics init end.....................");
    }

    public MetricRegistry getGaugeMetrics() {
        return metricRegistry;
    }
}
