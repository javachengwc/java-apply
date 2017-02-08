package com.concurrent.signal;

import com.util.base.ThreadUtil;

import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier
 * 与CountDownLatch不同，采用barrier.await加计数方式，当await的数量到达设定数量后，才继续往下执行
 * barrier 在释放等待线程后可以重用，所以称它为循环的barrier
 */
public class BarrierPrg {

    public static void main(String ... args)
    {
        new Performace(2);
    }
}

class Performace
{
    private int threadCount;

    private CyclicBarrier barrier;

    private int loopCount=10;

    public Performace(int threadCount)
    {
        this.threadCount=threadCount;

        barrier = new CyclicBarrier(2,new Runnable() {

            @Override
            public void run() {
                barrierDo();
            }
        });

        for(int i=0;i<threadCount;i++)
        {
            Thread thread =new Thread("per-thread"+i)
            {
                public void run()
                {
                    System.out.println(Thread.currentThread().getName()+" is running....");
                    for(int j=0;j<loopCount;j++) {
                        threadDo(j);
                        try{
                            System.out.println("barrier await start invoke");
                            barrier.await();

                            System.out.println("barrier await invoked");

                        }catch(Exception e)
                        {
                            e.printStackTrace(System.out);
                        }
                    }
                }
            };
            thread.start();
        }
    }

    public void threadDo(int j)
    {
        ThreadUtil.sleep(1000);
        System.out.println(Thread.currentThread().getName()+"---------第"+j+"次，threadDo invoke...----------------");
    }

    private void barrierDo()
    {
        ThreadUtil.sleep(1000);
        System.out.println(Thread.currentThread().getName()+" barrierDo invoke...");
    }
}