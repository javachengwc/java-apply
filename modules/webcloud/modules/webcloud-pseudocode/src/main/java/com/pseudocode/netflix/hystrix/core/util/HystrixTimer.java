package com.pseudocode.netflix.hystrix.core.util;
;
import com.pseudocode.netflix.hystrix.core.strategy.HystrixPlugins;
import com.pseudocode.netflix.hystrix.core.strategy.properties.HystrixPropertiesStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


//hystrix定时器
public class HystrixTimer {

    private static final Logger logger = LoggerFactory.getLogger(HystrixTimer.class);

    //单例
    private static HystrixTimer INSTANCE = new HystrixTimer();

    private HystrixTimer() {
    }

    public static HystrixTimer getInstance() {
        return INSTANCE;
    }

    public static void reset() {
        ScheduledExecutor ex = INSTANCE.executor.getAndSet(null);
        if (ex != null && ex.getThreadPool() != null) {
            ex.getThreadPool().shutdownNow();
        }
    }

    //定时任务执行器
    AtomicReference<ScheduledExecutor> executor = new AtomicReference<ScheduledExecutor>();

    //提交定时监听器，生成定时任务
    public Reference<TimerListener> addTimerListener(final TimerListener listener) {
        startThreadIfNeeded();
        // add the listener

        Runnable r = new Runnable() {

            @Override
            public void run() {
                try {
                    listener.tick();
                } catch (Exception e) {
                    logger.error("Failed while ticking TimerListener", e);
                }
            }
        };

        ScheduledFuture<?> f = executor.get().getThreadPool().scheduleAtFixedRate(r, listener.getIntervalTimeInMilliseconds(), listener.getIntervalTimeInMilliseconds(), TimeUnit.MILLISECONDS);
        return new TimerReference(listener, f);
    }

    private static class TimerReference extends SoftReference<TimerListener> {

        private final ScheduledFuture<?> f;

        TimerReference(TimerListener referent, ScheduledFuture<?> f) {
            super(referent);
            this.f = f;
        }

        @Override
        public void clear() {
            super.clear();
            // stop this ScheduledFuture from any further executions
            f.cancel(false);
        }

    }

    protected void startThreadIfNeeded() {
        // create and start thread if one doesn't exist
        while (executor.get() == null || ! executor.get().isInitialized()) {
            if (executor.compareAndSet(null, new ScheduledExecutor())) {
                // initialize the executor that we 'won' setting
                executor.get().initialize();
            }
        }
    }

    //执行线程池
    static class ScheduledExecutor {

        volatile ScheduledThreadPoolExecutor executor;

        private volatile boolean initialized;

        public void initialize() {

            HystrixPropertiesStrategy propertiesStrategy = HystrixPlugins.getInstance().getPropertiesStrategy();
            int coreSize = propertiesStrategy.getTimerThreadPoolProperties().getCorePoolSize().get();

            ThreadFactory threadFactory = null;
            if (!PlatformSpecific.isAppEngineStandardEnvironment()) {
                threadFactory = new ThreadFactory() {
                    final AtomicInteger counter = new AtomicInteger();

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r, "HystrixTimer-" + counter.incrementAndGet());
                        thread.setDaemon(true);
                        return thread;
                    }

                };
            } else {
                threadFactory = PlatformSpecific.getAppEngineThreadFactory();
            }

            executor = new ScheduledThreadPoolExecutor(coreSize, threadFactory);
            initialized = true;
        }

        public ScheduledThreadPoolExecutor getThreadPool() {
            return executor;
        }

        public boolean isInitialized() {
            return initialized;
        }
    }

    //定时监听器
    public static interface TimerListener {

        //时间到达超时执行的逻辑
        public void tick();

        //返回到达超时时间时长
        public int getIntervalTimeInMilliseconds();
    }

}