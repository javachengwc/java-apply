package com.pseudocode.netflix.ribbon.loadbalancer.server;

import com.pseudocode.netflix.eureka.core.util.MeasuredRate;
import com.pseudocode.netflix.ribbon.loadbalancer.LoadBalancerStats;
import com.google.common.annotations.VisibleForTesting;
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.servo.annotations.DataSourceType;
import com.netflix.servo.annotations.Monitor;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ServerStats {

    private static final int DEFAULT_PUBLISH_INTERVAL =  60 * 1000; // = 1 minute
    private static final int DEFAULT_BUFFER_SIZE = 60 * 1000; // = 1000 requests/sec for 1 minute
    private final DynamicIntProperty connectionFailureThreshold;
    private final DynamicIntProperty circuitTrippedTimeoutFactor;
    private final DynamicIntProperty maxCircuitTrippedTimeout;
    private static final DynamicIntProperty activeRequestsCountTimeout =
            DynamicPropertyFactory.getInstance().getIntProperty("niws.loadbalancer.serverStats.activeRequestsCount.effectiveWindowSeconds", 60 * 10);

    private static final double[] PERCENTS = makePercentValues();

    private DataDistribution dataDist = new DataDistribution(1, PERCENTS); // in case
    private DataPublisher publisher = null;
    private final Distribution responseTimeDist = new Distribution();

    int bufferSize = DEFAULT_BUFFER_SIZE;
    int publishInterval = DEFAULT_PUBLISH_INTERVAL;


    long failureCountSlidingWindowInterval = 1000;

    private MeasuredRate serverFailureCounts = new MeasuredRate(failureCountSlidingWindowInterval);
    private MeasuredRate requestCountInWindow = new MeasuredRate(300000L);

    Server server;

    AtomicLong totalRequests = new AtomicLong();

    @VisibleForTesting
    AtomicInteger successiveConnectionFailureCount = new AtomicInteger(0);

    @VisibleForTesting
    AtomicInteger activeRequestsCount = new AtomicInteger(0);

    @VisibleForTesting
    AtomicInteger openConnectionsCount = new AtomicInteger(0);

    private volatile long lastConnectionFailedTimestamp;
    private volatile long lastActiveRequestsCountChangeTimestamp;
    private AtomicLong totalCircuitBreakerBlackOutPeriod = new AtomicLong(0);
    private volatile long lastAccessedTimestamp;
    private volatile long firstConnectionTimestamp = 0;

    public ServerStats() {
        connectionFailureThreshold = DynamicPropertyFactory.getInstance().getIntProperty(
                "niws.loadbalancer.default.connectionFailureCountThreshold", 3);
        circuitTrippedTimeoutFactor = DynamicPropertyFactory.getInstance().getIntProperty(
                "niws.loadbalancer.default.circuitTripTimeoutFactorSeconds", 10);

        maxCircuitTrippedTimeout = DynamicPropertyFactory.getInstance().getIntProperty(
                "niws.loadbalancer.default.circuitTripMaxTimeoutSeconds", 30);
    }

    public ServerStats(LoadBalancerStats lbStats) {
        this.maxCircuitTrippedTimeout = lbStats.getCircuitTripMaxTimeoutSeconds();
        this.circuitTrippedTimeoutFactor = lbStats.getCircuitTrippedTimeoutFactor();
        this.connectionFailureThreshold = lbStats.getConnectionFailureCountThreshold();
    }

    public void initialize(Server server) {
        serverFailureCounts = new MeasuredRate(failureCountSlidingWindowInterval);
        requestCountInWindow = new MeasuredRate(300000L);
        if (publisher == null) {
            dataDist = new DataDistribution(getBufferSize(), PERCENTS);
            publisher = new DataPublisher(dataDist, getPublishIntervalMillis());
            publisher.start();
        }
        this.server = server;
    }

    public void close() {
        if (publisher != null)
            publisher.stop();
    }

    private int getBufferSize() {
        return bufferSize;
    }

    private long getPublishIntervalMillis() {
        return publishInterval;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public void setPublishInterval(int publishInterval) {
        this.publishInterval = publishInterval;
    }

    private static enum Percent {

        TEN(10), TWENTY_FIVE(25), FIFTY(50), SEVENTY_FIVE(75), NINETY(90),
        NINETY_FIVE(95), NINETY_EIGHT(98), NINETY_NINE(99), NINETY_NINE_POINT_FIVE(99.5);

        private double val;

        Percent(double val) {
            this.val = val;
        }

        public double getValue() {
            return val;
        }

    }

    private static double[] makePercentValues() {
        Percent[] percents = Percent.values();
        double[] p = new double[percents.length];
        for (int i = 0; i < percents.length; i++) {
            p[i] = percents[i].getValue();
        }
        return p;
    }

    public long getFailureCountSlidingWindowInterval() {
        return failureCountSlidingWindowInterval;
    }

    public void setFailureCountSlidingWindowInterval(
            long failureCountSlidingWindowInterval) {
        this.failureCountSlidingWindowInterval = failureCountSlidingWindowInterval;
    }

    // run time methods
    public void addToFailureCount(){
        serverFailureCounts.increment();
    }

    public long getFailureCount(){
        return serverFailureCounts.getCurrentCount();
    }

    public void noteResponseTime(double msecs){
        dataDist.noteValue(msecs);
        responseTimeDist.noteValue(msecs);
    }

    public void incrementNumRequests(){
        totalRequests.incrementAndGet();
    }

    public void incrementActiveRequestsCount() {
        activeRequestsCount.incrementAndGet();
        requestCountInWindow.increment();
        long currentTime = System.currentTimeMillis();
        lastActiveRequestsCountChangeTimestamp = currentTime;
        lastAccessedTimestamp = currentTime;
        if (firstConnectionTimestamp == 0) {
            firstConnectionTimestamp = currentTime;
        }
    }

    public void incrementOpenConnectionsCount() {
        openConnectionsCount.incrementAndGet();
    }

    public void decrementActiveRequestsCount() {
        if (activeRequestsCount.decrementAndGet() < 0) {
            activeRequestsCount.set(0);
        }
        lastActiveRequestsCountChangeTimestamp = System.currentTimeMillis();
    }

    public void decrementOpenConnectionsCount() {
        if (openConnectionsCount.decrementAndGet() < 0) {
            openConnectionsCount.set(0);
        }
    }

    public int  getActiveRequestsCount() {
        return getActiveRequestsCount(System.currentTimeMillis());
    }

    public int getActiveRequestsCount(long currentTime) {
        int count = activeRequestsCount.get();
        if (count == 0) {
            return 0;
        } else if (currentTime - lastActiveRequestsCountChangeTimestamp > activeRequestsCountTimeout.get() * 1000 || count < 0) {
            activeRequestsCount.set(0);
            return 0;
        } else {
            return count;
        }
    }

    public int getOpenConnectionsCount() {
        return openConnectionsCount.get();
    }


    public long getMeasuredRequestsCount() {
        return requestCountInWindow.getCount();
    }

    public int getMonitoredActiveRequestsCount() {
        return activeRequestsCount.get();
    }

    public boolean isCircuitBreakerTripped() {
        return isCircuitBreakerTripped(System.currentTimeMillis());
    }

    public boolean isCircuitBreakerTripped(long currentTime) {
        long circuitBreakerTimeout = getCircuitBreakerTimeout();
        if (circuitBreakerTimeout <= 0) {
            return false;
        }
        return circuitBreakerTimeout > currentTime;
    }


    private long getCircuitBreakerTimeout() {
        long blackOutPeriod = getCircuitBreakerBlackoutPeriod();
        if (blackOutPeriod <= 0) {
            return 0;
        }
        return lastConnectionFailedTimestamp + blackOutPeriod;
    }

    private long getCircuitBreakerBlackoutPeriod() {
        int failureCount = successiveConnectionFailureCount.get();
        int threshold = connectionFailureThreshold.get();
        if (failureCount < threshold) {
            return 0;
        }
        int diff = (failureCount - threshold) > 16 ? 16 : (failureCount - threshold);
        int blackOutSeconds = (1 << diff) * circuitTrippedTimeoutFactor.get();
        if (blackOutSeconds > maxCircuitTrippedTimeout.get()) {
            blackOutSeconds = maxCircuitTrippedTimeout.get();
        }
        return blackOutSeconds * 1000L;
    }

    public void incrementSuccessiveConnectionFailureCount() {
        lastConnectionFailedTimestamp = System.currentTimeMillis();
        successiveConnectionFailureCount.incrementAndGet();
        totalCircuitBreakerBlackOutPeriod.addAndGet(getCircuitBreakerBlackoutPeriod());
    }

    public void clearSuccessiveConnectionFailureCount() {
        successiveConnectionFailureCount.set(0);
    }

    public int getSuccessiveConnectionFailureCount() {
        return successiveConnectionFailureCount.get();
    }

    public double getResponseTimeAvg() {
        return responseTimeDist.getMean();
    }

    public double getResponseTimeMax() {
        return responseTimeDist.getMaximum();
    }

    public double getResponseTimeMin() {
        return responseTimeDist.getMinimum();
    }

    public double getResponseTimeStdDev() {
        return responseTimeDist.getStdDev();
    }

    public int getResponseTimePercentileNumValues() {
        return dataDist.getSampleSize();
    }

    public String getResponseTimePercentileTime() {
        return dataDist.getTimestamp();
    }

    public long getResponseTimePercentileTimeMillis() {
        return dataDist.getTimestampMillis();
    }

    public double getResponseTimeAvgRecent() {
        return dataDist.getMean();
    }

    public double getResponseTime10thPercentile() {
        return getResponseTimePercentile(Percent.TEN);
    }

    public double getResponseTime25thPercentile() {
        return getResponseTimePercentile(Percent.TWENTY_FIVE);
    }

    public double getResponseTime50thPercentile() {
        return getResponseTimePercentile(Percent.FIFTY);
    }

    public double getResponseTime75thPercentile() {
        return getResponseTimePercentile(Percent.SEVENTY_FIVE);
    }

    public double getResponseTime90thPercentile() {
        return getResponseTimePercentile(Percent.NINETY);
    }

    public double getResponseTime95thPercentile() {
        return getResponseTimePercentile(Percent.NINETY_FIVE);
    }

    public double getResponseTime98thPercentile() {
        return getResponseTimePercentile(Percent.NINETY_EIGHT);
    }

    public double getResponseTime99thPercentile() {
        return getResponseTimePercentile(Percent.NINETY_NINE);
    }

    public double getResponseTime99point5thPercentile() {
        return getResponseTimePercentile(Percent.NINETY_NINE_POINT_FIVE);
    }

    public long getTotalRequestsCount() {
        return totalRequests.get();
    }

    private double getResponseTimePercentile(Percent p) {
        return dataDist.getPercentiles()[p.ordinal()];
    }

}
