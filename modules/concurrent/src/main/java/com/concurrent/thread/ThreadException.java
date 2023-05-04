package com.concurrent.thread;

import com.util.base.RandomUtil;
import com.util.base.ThreadUtil;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

//线程池submit()方法可提交Runnable类型任务和Callable类型任务，任务执行的异常会被捕获并吞掉，在通过Future的get方法将任务执行时的异常重新抛出。
//线程池execute()方法只可提交Runnable类型任务，任务执行的异常会直接抛出，线程池会创建一个新的线程供使用。
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
