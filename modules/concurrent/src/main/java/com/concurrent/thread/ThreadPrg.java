package com.concurrent.thread;

import com.util.ThreadUtil;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * awaitTermination方法会使线程等待timeout时长，当超过timeout时间后，会监测ExecutorService是否已经关闭，若关闭则返回true，否则返回false
 */
public class ThreadPrg {

    public static void main(String ...args)
    {
        ExecutorService exeService= Executors.newFixedThreadPool(2);

        DealTask task1 = new DealTask(0);
        DealTask task2 = new DealTask(1);

        exeService.submit(task1);
        exeService.submit(task2);

        exeService.shutdown();

        long start = System.currentTimeMillis();
        System.out.println("-------------------threads start," + start);
        try {
            while (!exeService.awaitTermination(10, TimeUnit.SECONDS)) {
                //等待线程池执行完关闭
                System.out.println("线程池执行中");
            }
        }catch(Exception e)
        {
           e.printStackTrace(System.out);
        }
        System.out.println("线程池关闭");
        long cost = (System.currentTimeMillis()-start)/1000;
        System.out.println("-------------------threads end,cost " + cost + " sec");
    }

    public static class DealTask implements Callable<Object> {

        private int flag;

        public DealTask(int flag) {
            this.flag = flag;
        }

        @Override
        public Object call() throws Exception {
            ThreadUtil.sleep(1000 * 30);
            System.out.println(Thread.currentThread().getName() + " do end ,flag=" + flag);
            return null;
        }
    }
}
