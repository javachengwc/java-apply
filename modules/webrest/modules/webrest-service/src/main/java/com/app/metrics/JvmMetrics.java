package com.app.metrics;

import com.codahale.metrics.JvmAttributeGaugeSet;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;

@Component
public class JvmMetrics {

    private static Logger logger= LoggerFactory.getLogger(JvmMetrics.class);

    private  MetricRegistry metricRegistry = new MetricRegistry();

    public JvmMetrics() {
        init();
    }

    public void init() {
        logger.info("JvmMetrics init start.....................");
        //类加载器
        metricRegistry.register("jvm.classloader", new ClassLoadingGaugeSet());
        //垃圾回收
        metricRegistry.register("jvm.gc", new GarbageCollectorMetricSet());
        //内存
        metricRegistry.register("jvm.memory", new MemoryUsageGaugeSet());
        //线程
        metricRegistry.register("jvm.threads", new ThreadStatesGaugeSet());
        //文件句柄
        metricRegistry.register("jvm.filedescriptor", new FileDescriptorRatioGauge());
        //java虚拟机属性
        metricRegistry.register("jvm.attribute", new JvmAttributeGaugeSet());
        //buffers
        metricRegistry.register("jvm.buffers",new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));
        logger.info("JvmMetrics init end.....................");
    }

    public MetricRegistry getJvmMetrics() {
        return metricRegistry;
    }
}
