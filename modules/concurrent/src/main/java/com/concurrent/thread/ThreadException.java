package com.concurrent.thread;

import com.util.base.RandomUtil;
import com.util.base.ThreadUtil;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadException {

    public static void main(String [] args) {
        ThreadPoolExecutor exes = new ThreadPoolExecutor(2,2,0L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(100),
            new ThreadFactory() {
                final AtomicInteger poolNumber = new AtomicInteger(1);
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "Exe-" + poolNumber.getAndIncrement()+ RandomUtil.getRandomString(5));
                }
            },
            new ThreadPoolExecutor.CallerRunsPolicy());

        for(int i=0;i<10;i++) {
            exes.submit(new Runnable() {
                @Override
                public void run() {
                    String threadName =Thread.currentThread().getName();
                    System.out.println(threadName+"线程任务开始执行......");
                    ThreadUtil.sleep(200L);
                    if(threadName.contains("1")) {
                        System.out.println(threadName+"抛出异常");
                        throw new RuntimeException("new runtime exception");
                    }
                    System.out.println(threadName+"线程任务执行完成......");
                }
            });
        }
        System.out.println("任务都已提交到线程池执行");
    }
}
