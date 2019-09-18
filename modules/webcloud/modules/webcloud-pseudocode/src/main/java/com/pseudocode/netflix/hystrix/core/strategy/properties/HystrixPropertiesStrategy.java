package com.pseudocode.netflix.hystrix.core.strategy.properties;

import com.pseudocode.netflix.hystrix.core.HystrixCommandKey;
import com.pseudocode.netflix.hystrix.core.HystrixCommandProperties;
import com.pseudocode.netflix.hystrix.core.HystrixThreadPoolKey;
import com.pseudocode.netflix.hystrix.core.HystrixThreadPoolProperties;

public abstract class HystrixPropertiesStrategy {

    public HystrixCommandProperties getCommandProperties(HystrixCommandKey commandKey, HystrixCommandProperties.Setter builder) {
        return new HystrixPropertiesCommandDefault(commandKey, builder);
    }

    public String getCommandPropertiesCacheKey(HystrixCommandKey commandKey, HystrixCommandProperties.Setter builder) {
        return commandKey.name();
    }

    public HystrixThreadPoolProperties getThreadPoolProperties(HystrixThreadPoolKey threadPoolKey, HystrixThreadPoolProperties.Setter builder) {
        return new HystrixPropertiesThreadPoolDefault(threadPoolKey, builder);
    }

    public String getThreadPoolPropertiesCacheKey(HystrixThreadPoolKey threadPoolKey, HystrixThreadPoolProperties.Setter builder) {
        return threadPoolKey.name();
    }

    public HystrixCollapserProperties getCollapserProperties(HystrixCollapserKey collapserKey, HystrixCollapserProperties.Setter builder) {
        return new HystrixPropertiesCollapserDefault(collapserKey, builder);
    }

    public String getCollapserPropertiesCacheKey(HystrixCollapserKey collapserKey, HystrixCollapserProperties.Setter builder) {
        return collapserKey.name();
    }

    public HystrixTimerThreadPoolProperties getTimerThreadPoolProperties() {
        return new HystrixPropertiesTimerThreadPoolDefault();
    }
}