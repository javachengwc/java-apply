package com.concurrent.limit;

import com.google.common.util.concurrent.RateLimiter;
import com.util.base.ThreadUtil;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LimitMain {

    public static void main(String [] args ) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1,2,0L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(10),
            new ThreadFactory() {
                final AtomicInteger poolNumber = new AtomicInteger(1);
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "executor-" + poolNumber.getAndIncrement());
                }
            },
            new ThreadPoolExecutor.CallerRunsPolicy());

        //限流器设定qps
        RateLimiter limiter = RateLimiter.create(1);
        AtomicLong prev = new AtomicLong(System.currentTimeMillis());

        for (int i = 0; i < 20; i++) {
            executor.execute(() -> {
                //限流器限流
                limiter.acquire();
                long cur = System.currentTimeMillis();
                //打印时间间隔：毫秒
                ThreadUtil.sleep(200L);
                System.out.println("线程->"+Thread.currentThread().getName()+",时间戳->"+cur+" "+(cur - prev.get()));
                prev.set(cur);
            });
        }
        System.out.println("ccccccccccc");
        try {
            executor.shutdown();
            while (!executor.awaitTermination(100, TimeUnit.MILLISECONDS)) {
                long taskCount = executor.getTaskCount();
                long completedCount = executor.getCompletedTaskCount();
                int activeThreadCount = executor.getActiveCount();
                int queueTaskSize = executor.getQueue().size();
                System.out.println("线程池执行中,"+taskCount+" "+completedCount+" "+ activeThreadCount+" "+queueTaskSize);
            }
        }catch(Exception e) {
            e.printStackTrace(System.out);
        }

        System.out.println("exe end");
    }
}
