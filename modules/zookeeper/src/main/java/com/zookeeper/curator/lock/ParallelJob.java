package com.zookeeper.curator.lock;

import org.apache.curator.framework.recipes.locks.InterProcessLock;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 *  并行任务
 */
public class ParallelJob implements Runnable {

    private final String name;

    private final InterProcessLock lock;

    // 锁等待时间
    private final int wait_time = 5;

    ParallelJob(String name, InterProcessLock lock) {
        this.name = name;
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            doWork();
        } catch (Exception e) {
           e.printStackTrace(System.out);
        }
    }

    public void doWork() throws Exception {
        try {
            if (!lock.acquire(wait_time, TimeUnit.SECONDS)) {
                System.err.println(name + "等待" + wait_time + "秒，仍未能获取到lock,准备放弃。");
            }
            //job执行时间0-4000毫秒
            int exeTime = new Random().nextInt(4000);
            System.out.println(name + "开始执行,预计执行时间= " + exeTime + "毫秒----------");
            Thread.sleep(exeTime);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            lock.release();
        }
    }
}