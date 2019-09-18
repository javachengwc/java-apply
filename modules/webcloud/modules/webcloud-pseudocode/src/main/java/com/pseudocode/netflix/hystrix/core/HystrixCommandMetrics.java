package com.pseudocode.netflix.hystrix.core;


import com.pseudocode.netflix.hystrix.core.metric.consumer.HealthCountsStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.functions.Func0;
import rx.functions.Func2;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class HystrixCommandMetrics extends HystrixMetrics {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(HystrixCommandMetrics.class);

    private static final HystrixEventType[] ALL_EVENT_TYPES = HystrixEventType.values();

    public static final Func2<long[], HystrixCommandCompletion, long[]> appendEventToBucket = new Func2<long[], HystrixCommandCompletion, long[]>() {
        @Override
        public long[] call(long[] initialCountArray, HystrixCommandCompletion execution) {
            ExecutionResult.EventCounts eventCounts = execution.getEventCounts();
            for (HystrixEventType eventType: ALL_EVENT_TYPES) {
                switch (eventType) {
                    case EXCEPTION_THROWN: break; //this is just a sum of other anyway - don't do the work here
                    default:
                        initialCountArray[eventType.ordinal()] += eventCounts.getCount(eventType);
                        break;
                }
            }
            return initialCountArray;
        }
    };

    public static final Func2<long[], long[], long[]> bucketAggregator = new Func2<long[], long[], long[]>() {
        @Override
        public long[] call(long[] cumulativeEvents, long[] bucketEventCounts) {
            for (HystrixEventType eventType: ALL_EVENT_TYPES) {
                switch (eventType) {
                    case EXCEPTION_THROWN:
                        for (HystrixEventType exceptionEventType: HystrixEventType.EXCEPTION_PRODUCING_EVENT_TYPES) {
                            cumulativeEvents[eventType.ordinal()] += bucketEventCounts[exceptionEventType.ordinal()];
                        }
                        break;
                    default:
                        cumulativeEvents[eventType.ordinal()] += bucketEventCounts[eventType.ordinal()];
                        break;
                }
            }
            return cumulativeEvents;
        }
    };

    // String is HystrixCommandKey.name() (we can't use HystrixCommandKey directly as we can't guarantee it implements hashcode/equals correctly)
    private static final ConcurrentHashMap<String, HystrixCommandMetrics> metrics = new ConcurrentHashMap<String, HystrixCommandMetrics>();

    public static HystrixCommandMetrics getInstance(HystrixCommandKey key, HystrixCommandGroupKey commandGroup, HystrixCommandProperties properties) {
        return getInstance(key, commandGroup, null, properties);
    }

    public static HystrixCommandMetrics getInstance(HystrixCommandKey key, HystrixCommandGroupKey commandGroup, HystrixThreadPoolKey threadPoolKey, HystrixCommandProperties properties) {
        // attempt to retrieve from cache first
        HystrixCommandMetrics commandMetrics = metrics.get(key.name());
        if (commandMetrics != null) {
            return commandMetrics;
        } else {
            synchronized (HystrixCommandMetrics.class) {
                HystrixCommandMetrics existingMetrics = metrics.get(key.name());
                if (existingMetrics != null) {
                    return existingMetrics;
                } else {
                    HystrixThreadPoolKey nonNullThreadPoolKey;
                    if (threadPoolKey == null) {
                        nonNullThreadPoolKey = HystrixThreadPoolKey.Factory.asKey(commandGroup.name());
                    } else {
                        nonNullThreadPoolKey = threadPoolKey;
                    }
                    HystrixCommandMetrics newCommandMetrics = new HystrixCommandMetrics(key, commandGroup, nonNullThreadPoolKey, properties, HystrixPlugins.getInstance().getEventNotifier());
                    metrics.putIfAbsent(key.name(), newCommandMetrics);
                    return newCommandMetrics;
                }
            }
        }
    }

    public static HystrixCommandMetrics getInstance(HystrixCommandKey key) {
        return metrics.get(key.name());
    }

    public static Collection<HystrixCommandMetrics> getInstances() {
        return Collections.unmodifiableCollection(metrics.values());
    }

    static void reset() {
        for (HystrixCommandMetrics metricsInstance: getInstances()) {
            metricsInstance.unsubscribeAll();
        }
        metrics.clear();
    }

    private final HystrixCommandProperties properties;
    private final HystrixCommandKey key;
    private final HystrixCommandGroupKey group;
    private final HystrixThreadPoolKey threadPoolKey;
    private final AtomicInteger concurrentExecutionCount = new AtomicInteger();

    private HealthCountsStream healthCountsStream;

    private final RollingCommandEventCounterStream rollingCommandEventCounterStream;
    private final CumulativeCommandEventCounterStream cumulativeCommandEventCounterStream;
    private final RollingCommandLatencyDistributionStream rollingCommandLatencyDistributionStream;
    private final RollingCommandUserLatencyDistributionStream rollingCommandUserLatencyDistributionStream;
    private final RollingCommandMaxConcurrencyStream rollingCommandMaxConcurrencyStream;

    HystrixCommandMetrics(final HystrixCommandKey key, HystrixCommandGroupKey commandGroup, HystrixThreadPoolKey threadPoolKey, HystrixCommandProperties properties, HystrixEventNotifier eventNotifier) {
        super(null);
        this.key = key;
        this.group = commandGroup;
        this.threadPoolKey = threadPoolKey;
        this.properties = properties;

        healthCountsStream = HealthCountsStream.getInstance(key, properties);

        rollingCommandEventCounterStream = RollingCommandEventCounterStream.getInstance(key, properties);
        cumulativeCommandEventCounterStream = CumulativeCommandEventCounterStream.getInstance(key, properties);

        rollingCommandLatencyDistributionStream = RollingCommandLatencyDistributionStream.getInstance(key, properties);
        rollingCommandUserLatencyDistributionStream = RollingCommandUserLatencyDistributionStream.getInstance(key, properties);
        rollingCommandMaxConcurrencyStream = RollingCommandMaxConcurrencyStream.getInstance(key, properties);
    }

    synchronized void resetStream() {
        healthCountsStream.unsubscribe();
        HealthCountsStream.removeByKey(key);
        healthCountsStream = HealthCountsStream.getInstance(key, properties);
    }

    public HystrixCommandKey getCommandKey() {
        return key;
    }

    public HystrixCommandGroupKey getCommandGroup() {
        return group;
    }

    public HystrixThreadPoolKey getThreadPoolKey() {
        return threadPoolKey;
    }

    public HystrixCommandProperties getProperties() {
        return properties;
    }

    public long getRollingCount(HystrixEventType eventType) {
        return rollingCommandEventCounterStream.getLatest(eventType);
    }

    public long getCumulativeCount(HystrixEventType eventType) {
        return cumulativeCommandEventCounterStream.getLatest(eventType);
    }

    @Override
    public long getCumulativeCount(HystrixRollingNumberEvent event) {
        return getCumulativeCount(HystrixEventType.from(event));
    }

    @Override
    public long getRollingCount(HystrixRollingNumberEvent event) {
        return getRollingCount(HystrixEventType.from(event));
    }

    public int getExecutionTimePercentile(double percentile) {
        return rollingCommandLatencyDistributionStream.getLatestPercentile(percentile);
    }

    public int getExecutionTimeMean() {
        return rollingCommandLatencyDistributionStream.getLatestMean();
    }

    public int getTotalTimePercentile(double percentile) {
        return rollingCommandUserLatencyDistributionStream.getLatestPercentile(percentile);
    }

    public int getTotalTimeMean() {
        return rollingCommandUserLatencyDistributionStream.getLatestMean();
    }

    public long getRollingMaxConcurrentExecutions() {
        return rollingCommandMaxConcurrencyStream.getLatestRollingMax();
    }

    public int getCurrentConcurrentExecutionCount() {
        return concurrentExecutionCount.get();
    }

    void markCommandStart(HystrixCommandKey commandKey, HystrixThreadPoolKey threadPoolKey, HystrixCommandProperties.ExecutionIsolationStrategy isolationStrategy) {
        int currentCount = concurrentExecutionCount.incrementAndGet();
        HystrixThreadEventStream.getInstance().commandExecutionStarted(commandKey, threadPoolKey, isolationStrategy, currentCount);
    }

    void markCommandDone(ExecutionResult executionResult, HystrixCommandKey commandKey, HystrixThreadPoolKey threadPoolKey, boolean executionStarted) {
        HystrixThreadEventStream.getInstance().executionDone(executionResult, commandKey, threadPoolKey);
        if (executionStarted) {
            concurrentExecutionCount.decrementAndGet();
        }
    }

    HealthCountsStream getHealthCountsStream() {
        return healthCountsStream;
    }

    public HealthCounts getHealthCounts() {
        return healthCountsStream.getLatest();
    }

    private void unsubscribeAll() {
        healthCountsStream.unsubscribe();
        rollingCommandEventCounterStream.unsubscribe();
        cumulativeCommandEventCounterStream.unsubscribe();
        rollingCommandLatencyDistributionStream.unsubscribe();
        rollingCommandUserLatencyDistributionStream.unsubscribe();
        rollingCommandMaxConcurrencyStream.unsubscribe();
    }

    public static class HealthCounts {
        private final long totalCount;
        private final long errorCount;
        private final int errorPercentage;

        HealthCounts(long total, long error) {
            this.totalCount = total;
            this.errorCount = error;
            if (totalCount > 0) {
                this.errorPercentage = (int) ((double) errorCount / totalCount * 100);
            } else {
                this.errorPercentage = 0;
            }
        }

        private static final HealthCounts EMPTY = new HealthCounts(0, 0);

        public long getTotalRequests() {
            return totalCount;
        }

        public long getErrorCount() {
            return errorCount;
        }

        public int getErrorPercentage() {
            return errorPercentage;
        }

        public HealthCounts plus(long[] eventTypeCounts) {
            long updatedTotalCount = totalCount;
            long updatedErrorCount = errorCount;

            long successCount = eventTypeCounts[HystrixEventType.SUCCESS.ordinal()];
            long failureCount = eventTypeCounts[HystrixEventType.FAILURE.ordinal()];
            long timeoutCount = eventTypeCounts[HystrixEventType.TIMEOUT.ordinal()];
            long threadPoolRejectedCount = eventTypeCounts[HystrixEventType.THREAD_POOL_REJECTED.ordinal()];
            long semaphoreRejectedCount = eventTypeCounts[HystrixEventType.SEMAPHORE_REJECTED.ordinal()];

            updatedTotalCount += (successCount + failureCount + timeoutCount + threadPoolRejectedCount + semaphoreRejectedCount);
            updatedErrorCount += (failureCount + timeoutCount + threadPoolRejectedCount + semaphoreRejectedCount);
            return new HealthCounts(updatedTotalCount, updatedErrorCount);
        }

        public static HealthCounts empty() {
            return EMPTY;
        }

        public String toString() {
            return "HealthCounts[" + errorCount + " / " + totalCount + " : " + getErrorPercentage() + "%]";
        }
    }
}
