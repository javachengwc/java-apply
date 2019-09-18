package com.pseudocode.netflix.hystrix.core.strategy.concurrency;

import com.pseudocode.netflix.hystrix.core.HystrixThreadPoolKey;
import com.pseudocode.netflix.hystrix.core.HystrixThreadPoolProperties;
import com.pseudocode.netflix.hystrix.core.strategy.properties.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class HystrixConcurrencyStrategy {

    private final static Logger logger = LoggerFactory.getLogger(HystrixConcurrencyStrategy.class);

    public ThreadPoolExecutor getThreadPool(final HystrixThreadPoolKey threadPoolKey, HystrixProperty<Integer> corePoolSize,
                                            HystrixProperty<Integer> maximumPoolSize, HystrixProperty<Integer> keepAliveTime,
                                            TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        final ThreadFactory threadFactory = getThreadFactory(threadPoolKey);

        final int dynamicCoreSize = corePoolSize.get();
        final int dynamicMaximumSize = maximumPoolSize.get();

        if (dynamicCoreSize > dynamicMaximumSize) {
            logger.error("Hystrix ThreadPool configuration at startup for : " + threadPoolKey.name() + " is trying to set coreSize = " +
                    dynamicCoreSize + " and maximumSize = " + dynamicMaximumSize + ".  Maximum size will be set to " +
                    dynamicCoreSize + ", the coreSize value, since it must be equal to or greater than the coreSize value");
            return new ThreadPoolExecutor(dynamicCoreSize, dynamicCoreSize, keepAliveTime.get(), unit, workQueue, threadFactory);
        } else {
            return new ThreadPoolExecutor(dynamicCoreSize, dynamicMaximumSize, keepAliveTime.get(), unit, workQueue, threadFactory);
        }
    }

    public ThreadPoolExecutor getThreadPool(final HystrixThreadPoolKey threadPoolKey, HystrixThreadPoolProperties threadPoolProperties) {
        final ThreadFactory threadFactory = getThreadFactory(threadPoolKey);

        final boolean allowMaximumSizeToDivergeFromCoreSize = threadPoolProperties.getAllowMaximumSizeToDivergeFromCoreSize().get();
        final int dynamicCoreSize = threadPoolProperties.coreSize().get();
        final int keepAliveTime = threadPoolProperties.keepAliveTimeMinutes().get();
        final int maxQueueSize = threadPoolProperties.maxQueueSize().get();
        final BlockingQueue<Runnable> workQueue = getBlockingQueue(maxQueueSize);

        if (allowMaximumSizeToDivergeFromCoreSize) {
            final int dynamicMaximumSize = threadPoolProperties.maximumSize().get();
            if (dynamicCoreSize > dynamicMaximumSize) {
                logger.error("Hystrix ThreadPool configuration at startup for : " + threadPoolKey.name() + " is trying to set coreSize = " +
                        dynamicCoreSize + " and maximumSize = " + dynamicMaximumSize + ".  Maximum size will be set to " +
                        dynamicCoreSize + ", the coreSize value, since it must be equal to or greater than the coreSize value");
                return new ThreadPoolExecutor(dynamicCoreSize, dynamicCoreSize, keepAliveTime, TimeUnit.MINUTES, workQueue, threadFactory);
            } else {
                return new ThreadPoolExecutor(dynamicCoreSize, dynamicMaximumSize, keepAliveTime, TimeUnit.MINUTES, workQueue, threadFactory);
            }
        } else {
            return new ThreadPoolExecutor(dynamicCoreSize, dynamicCoreSize, keepAliveTime, TimeUnit.MINUTES, workQueue, threadFactory);
        }
    }

    private static ThreadFactory getThreadFactory(final HystrixThreadPoolKey threadPoolKey) {
        if (!PlatformSpecific.isAppEngineStandardEnvironment()) {
            return new ThreadFactory() {
                private final AtomicInteger threadNumber = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r, "hystrix-" + threadPoolKey.name() + "-" + threadNumber.incrementAndGet());
                    thread.setDaemon(true);
                    return thread;
                }

            };
        } else {
            return PlatformSpecific.getAppEngineThreadFactory();
        }
    }

    public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) {
        if (maxQueueSize <= 0) {
            return new SynchronousQueue<Runnable>();
        } else {
            return new LinkedBlockingQueue<Runnable>(maxQueueSize);
        }
    }

    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        return callable;
    }

    public <T> HystrixRequestVariable<T> getRequestVariable(final HystrixRequestVariableLifecycle<T> rv) {
        return new HystrixLifecycleForwardingRequestVariable<T>(rv);
    }

}
