package com.concurrent.signal;

import com.util.base.ThreadUtil;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch
 * 可用于控制多个线程同时开始某动作，采用方式为countDown减计数方式，当计数为0时，latch.await后的代码才执行
 */
public class CountDownPrg {

    public static CountDownLatch doLatch = new CountDownLatch(10);

    public static CountDownLatch gatherLatch = new CountDownLatch(1);

    public static void main(String ... args) throws Exception
    {
        complateDo();
    }

    //等待多个线程结束再做后续作业
    public static void complateDo() throws Exception
    {
        for(int i=0;i<10;i++)
        {
            Thread thread = new Thread("pre thread"+i)
            {
                public void run()
                {
                    try{
                        ThreadUtil.sleep(1000);
                        System.out.println(Thread.currentThread().getName()+" is done");

                        doLatch.countDown();

                    }catch(Exception e)
                    {
                        e.printStackTrace(System.out);
                    }
                }
            };
            thread.start();

        }

        doLatch.await();

        System.out.print("前序多线程工作已完成，开始后续作业...");

        gatherStart();
    }

    //多个线程得到通知后一起启动
    public static void gatherStart()
    {
        for(int i=0;i<10;i++)
        {
            Thread thread = new Thread("worker thread"+i)
            {
                public void run()
                {
                    try{
                        gatherLatch.await();

                        System.out.println(Thread.currentThread().getName() + " is dong...");

                    }catch(Exception e)
                    {
                        e.printStackTrace(System.out);
                    }
                }
            };
            thread.start();

        }

        ThreadUtil.sleep(1000);
        System.out.println("多个线程一起启动通知开始");
        gatherLatch.countDown();
    }
}
