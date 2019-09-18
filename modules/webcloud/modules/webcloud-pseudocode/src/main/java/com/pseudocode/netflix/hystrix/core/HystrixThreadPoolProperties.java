package com.pseudocode.netflix.hystrix.core;

import com.pseudocode.netflix.hystrix.core.strategy.properties.HystrixProperty;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//hystrix线程池属性
public abstract class HystrixThreadPoolProperties {

    /* defaults */
    static int default_coreSize = 10;            // core size of thread pool
    static int default_maximumSize = 10;         // maximum size of thread pool
    static int default_keepAliveTimeMinutes = 1; // minutes to keep a thread alive
    static int default_maxQueueSize = -1;        // size of queue (this can't be dynamically changed so we use 'queueSizeRejectionThreshold' to artificially limit and reject)
    // -1 turns it off and makes us use SynchronousQueue
    static boolean default_allow_maximum_size_to_diverge_from_core_size = false; //should the maximumSize config value get read and used in configuring the threadPool
    //turning this on should be a conscious decision by the user, so we default it to false

    static int default_queueSizeRejectionThreshold = 5; // number of items in queue
    static int default_threadPoolRollingNumberStatisticalWindow = 10000; // milliseconds for rolling number
    static int default_threadPoolRollingNumberStatisticalWindowBuckets = 10; // number of buckets in rolling number (10 1-second buckets)

    private final HystrixProperty<Integer> corePoolSize;
    private final HystrixProperty<Integer> maximumPoolSize;
    private final HystrixProperty<Integer> keepAliveTime;
    private final HystrixProperty<Integer> maxQueueSize;
    private final HystrixProperty<Integer> queueSizeRejectionThreshold;
    private final HystrixProperty<Boolean> allowMaximumSizeToDivergeFromCoreSize;

    private final HystrixProperty<Integer> threadPoolRollingNumberStatisticalWindowInMilliseconds;
    private final HystrixProperty<Integer> threadPoolRollingNumberStatisticalWindowBuckets;

    protected HystrixThreadPoolProperties(HystrixThreadPoolKey key) {
        this(key, new Setter(), "hystrix");
    }

    protected HystrixThreadPoolProperties(HystrixThreadPoolKey key, Setter builder) {
        this(key, builder, "hystrix");
    }

    protected HystrixThreadPoolProperties(HystrixThreadPoolKey key, Setter builder, String propertyPrefix) {
        this.allowMaximumSizeToDivergeFromCoreSize = getProperty(propertyPrefix, key, "allowMaximumSizeToDivergeFromCoreSize",
                builder.getAllowMaximumSizeToDivergeFromCoreSize(), default_allow_maximum_size_to_diverge_from_core_size);

        this.corePoolSize = getProperty(propertyPrefix, key, "coreSize", builder.getCoreSize(), default_coreSize);
        //this object always contains a reference to the configuration value for the maximumSize of the threadpool
        //it only gets applied if allowMaximumSizeToDivergeFromCoreSize is true
        this.maximumPoolSize = getProperty(propertyPrefix, key, "maximumSize", builder.getMaximumSize(), default_maximumSize);

        this.keepAliveTime = getProperty(propertyPrefix, key, "keepAliveTimeMinutes", builder.getKeepAliveTimeMinutes(), default_keepAliveTimeMinutes);
        this.maxQueueSize = getProperty(propertyPrefix, key, "maxQueueSize", builder.getMaxQueueSize(), default_maxQueueSize);
        this.queueSizeRejectionThreshold = getProperty(propertyPrefix, key, "queueSizeRejectionThreshold", builder.getQueueSizeRejectionThreshold(), default_queueSizeRejectionThreshold);
        this.threadPoolRollingNumberStatisticalWindowInMilliseconds = getProperty(propertyPrefix, key, "metrics.rollingStats.timeInMilliseconds", builder.getMetricsRollingStatisticalWindowInMilliseconds(), default_threadPoolRollingNumberStatisticalWindow);
        this.threadPoolRollingNumberStatisticalWindowBuckets = getProperty(propertyPrefix, key, "metrics.rollingStats.numBuckets", builder.getMetricsRollingStatisticalWindowBuckets(), default_threadPoolRollingNumberStatisticalWindowBuckets);
    }

    private static HystrixProperty<Integer> getProperty(String propertyPrefix, HystrixThreadPoolKey key, String instanceProperty, Integer builderOverrideValue, Integer defaultValue) {
        return forInteger()
                .add(propertyPrefix + ".threadpool." + key.name() + "." + instanceProperty, builderOverrideValue)
                .add(propertyPrefix + ".threadpool.default." + instanceProperty, defaultValue)
                .build();
    }

    private static HystrixProperty<Boolean> getProperty(String propertyPrefix, HystrixThreadPoolKey key, String instanceProperty, Boolean builderOverrideValue, Boolean defaultValue) {
        return forBoolean()
                .add(propertyPrefix + ".threadpool." + key.name() + "." + instanceProperty, builderOverrideValue)
                .add(propertyPrefix + ".threadpool.default." + instanceProperty, defaultValue)
                .build();
    }

    public HystrixProperty<Integer> coreSize() {
        return corePoolSize;
    }

    public HystrixProperty<Integer> maximumSize() {
        return maximumPoolSize;
    }

    public Integer actualMaximumSize() {
        final int coreSize = coreSize().get();
        final int maximumSize = maximumSize().get();
        if (getAllowMaximumSizeToDivergeFromCoreSize().get()) {
            if (coreSize > maximumSize) {
                return coreSize;
            } else {
                return maximumSize;
            }
        } else {
            return coreSize;
        }
    }

    public HystrixProperty<Integer> keepAliveTimeMinutes() {
        return keepAliveTime;
    }

    public HystrixProperty<Integer> maxQueueSize() {
        return maxQueueSize;
    }

    public HystrixProperty<Integer> queueSizeRejectionThreshold() {
        return queueSizeRejectionThreshold;
    }

    public HystrixProperty<Boolean> getAllowMaximumSizeToDivergeFromCoreSize() {
        return allowMaximumSizeToDivergeFromCoreSize;
    }

    public HystrixProperty<Integer> metricsRollingStatisticalWindowInMilliseconds() {
        return threadPoolRollingNumberStatisticalWindowInMilliseconds;
    }

    public HystrixProperty<Integer> metricsRollingStatisticalWindowBuckets() {
        return threadPoolRollingNumberStatisticalWindowBuckets;
    }

    public static Setter Setter() {
        return new Setter();
    }

    public static Setter defaultSetter() {
        return Setter();
    }

    public static class Setter {
        private Integer coreSize = null;
        private Integer maximumSize = null;
        private Integer keepAliveTimeMinutes = null;
        private Integer maxQueueSize = null;
        private Integer queueSizeRejectionThreshold = null;
        private Boolean allowMaximumSizeToDivergeFromCoreSize = null;
        private Integer rollingStatisticalWindowInMilliseconds = null;
        private Integer rollingStatisticalWindowBuckets = null;

        private Setter() {
        }

        public Integer getCoreSize() {
            return coreSize;
        }

        public Integer getMaximumSize() {
            return maximumSize;
        }

        public Integer getKeepAliveTimeMinutes() {
            return keepAliveTimeMinutes;
        }

        public Integer getMaxQueueSize() {
            return maxQueueSize;
        }

        public Integer getQueueSizeRejectionThreshold() {
            return queueSizeRejectionThreshold;
        }

        public Boolean getAllowMaximumSizeToDivergeFromCoreSize() {
            return allowMaximumSizeToDivergeFromCoreSize;
        }

        public Integer getMetricsRollingStatisticalWindowInMilliseconds() {
            return rollingStatisticalWindowInMilliseconds;
        }

        public Integer getMetricsRollingStatisticalWindowBuckets() {
            return rollingStatisticalWindowBuckets;
        }

        public Setter withCoreSize(int value) {
            this.coreSize = value;
            return this;
        }

        public Setter withMaximumSize(int value) {
            this.maximumSize = value;
            return this;
        }

        public Setter withKeepAliveTimeMinutes(int value) {
            this.keepAliveTimeMinutes = value;
            return this;
        }

        public Setter withMaxQueueSize(int value) {
            this.maxQueueSize = value;
            return this;
        }

        public Setter withQueueSizeRejectionThreshold(int value) {
            this.queueSizeRejectionThreshold = value;
            return this;
        }

        public Setter withAllowMaximumSizeToDivergeFromCoreSize(boolean value) {
            this.allowMaximumSizeToDivergeFromCoreSize = value;
            return this;
        }

        public Setter withMetricsRollingStatisticalWindowInMilliseconds(int value) {
            this.rollingStatisticalWindowInMilliseconds = value;
            return this;
        }

        public Setter withMetricsRollingStatisticalWindowBuckets(int value) {
            this.rollingStatisticalWindowBuckets = value;
            return this;
        }
    }
}

