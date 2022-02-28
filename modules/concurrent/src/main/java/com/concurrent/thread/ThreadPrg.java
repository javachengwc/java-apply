package com.concurrent.thread;

import com.util.base.ThreadUtil;

import java.util.concurrent.*;

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
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) exeService;
                //任务总数
                long taskCount = threadPoolExecutor.getTaskCount();
                //完成的任务数
                long completedCount = threadPoolExecutor.getCompletedTaskCount();
                //对列中的任务数
                int queueTaskSize = threadPoolExecutor.getQueue().size();
                //活动的线程数
                int activeThreadCount = threadPoolExecutor.getActiveCount();
                //等待线程池执行完关闭
                System.out.println("线程池执行中,"+taskCount+" "+completedCount+" "+ activeThreadCount);
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
