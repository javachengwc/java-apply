package com.pseudocode.netflix.hystrix.core;


public abstract class HystrixMetrics {

    protected final HystrixRollingNumber counter;

    protected HystrixMetrics(HystrixRollingNumber counter) {
        this.counter = counter;
    }

    public long getCumulativeCount(HystrixRollingNumberEvent event) {
        return counter.getCumulativeSum(event);
    }

    public long getRollingCount(HystrixRollingNumberEvent event) {
        return counter.getRollingSum(event);
    }

}
