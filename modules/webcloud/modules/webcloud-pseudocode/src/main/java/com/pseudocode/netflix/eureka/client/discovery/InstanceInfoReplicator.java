package com.pseudocode.netflix.eureka.client.discovery;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.pseudocode.netflix.eureka.client.appinfo.InstanceInfo;
import com.pseudocode.netflix.eureka.client.discovery.util.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

//A task for updating and replicating the local instanceinfo to the remote
//应用实例信息复制器
class InstanceInfoReplicator implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(InstanceInfoReplicator.class);

    private final DiscoveryClient discoveryClient;

    //应用实例信息
    private final InstanceInfo instanceInfo;

    //定时执行频率，单位：秒
    private final int replicationIntervalSeconds;

    //定时执行器
    private final ScheduledExecutorService scheduler;

    //定时执行任务的 Future
    private final AtomicReference<Future> scheduledPeriodicRef;

    //是否开启调度
    private final AtomicBoolean started;

    private final RateLimiter rateLimiter;

    private final int burstSize;
    private final int allowedRatePerMinute; // 限流相关

    InstanceInfoReplicator(DiscoveryClient discoveryClient, InstanceInfo instanceInfo, int replicationIntervalSeconds, int burstSize) {
        this.discoveryClient = discoveryClient;
        this.instanceInfo = instanceInfo;
        this.scheduler = Executors.newScheduledThreadPool(1,
                new ThreadFactoryBuilder()
                        .setNameFormat("DiscoveryClient-InstanceInfoReplicator-%d")
                        .setDaemon(true)
                        .build());

        this.scheduledPeriodicRef = new AtomicReference<Future>();

        this.started = new AtomicBoolean(false);
        this.rateLimiter = new RateLimiter(TimeUnit.MINUTES);
        this.replicationIntervalSeconds = replicationIntervalSeconds;
        this.burstSize = burstSize;

        this.allowedRatePerMinute = 60 * this.burstSize / this.replicationIntervalSeconds;
        logger.info("InstanceInfoReplicator onDemand update allowed rate per min is {}", allowedRatePerMinute);
    }

    public void start(int initialDelayMs) {
        if (started.compareAndSet(false, true)) {
            //设置应用实例信息数据不一致
            instanceInfo.setIsDirty();  // for initial register
            // 提交任务并设置该任务的 Future
            Future next = scheduler.schedule(this, initialDelayMs, TimeUnit.SECONDS);
            scheduledPeriodicRef.set(next);
        }
    }

    public void stop() {
        shutdownAndAwaitTermination(scheduler);
        started.set(false);
    }

    private void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(3, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            logger.warn("InstanceInfoReplicator stop interrupted");
        }
    }

    //实例状态变更时候，监听器中调用次方法
    public boolean onDemandUpdate() {
        if (rateLimiter.acquire(burstSize, allowedRatePerMinute)) {
                if (!scheduler.isShutdown()) {
                scheduler.submit(new Runnable() {
                    @Override
                    public void run() {
                        logger.debug("Executing on-demand update of local InstanceInfo");

                        Future latestPeriodic = scheduledPeriodicRef.get();
                        if (latestPeriodic != null && !latestPeriodic.isDone()) {
                            // 取消之前还没完成的任务
                            logger.debug("Canceling the latest scheduled update, it will be rescheduled at the end of on demand update");
                            latestPeriodic.cancel(false);
                        }
                        //再次调用刷新应用实例信息到Eureka server
                        InstanceInfoReplicator.this.run();
                    }
                });
                return true;
            } else {
                logger.warn("Ignoring onDemand update due to stopped scheduler");
                return false;
            }
        } else {
            logger.warn("Ignoring onDemand update due to rate limiter");
            return false;
        }
    }

    public void run() {
        try {
            //刷新应用实例信息
            discoveryClient.refreshInstanceInfo();

            //判断应用实例信息是否数据不一致
            Long dirtyTimestamp = instanceInfo.isDirtyWithTime();
            if (dirtyTimestamp != null) {
                //发起注册
                discoveryClient.register();
                //设置应用实例信息 数据一致
                instanceInfo.unsetIsDirty(dirtyTimestamp);
            }
        } catch (Throwable t) {
            logger.warn("There was a problem with the instance info replicator", t);
        } finally {
            //提交任务，并设置该任务的 Future
            Future next = scheduler.schedule(this, replicationIntervalSeconds, TimeUnit.SECONDS);
            scheduledPeriodicRef.set(next);
        }
    }

}
