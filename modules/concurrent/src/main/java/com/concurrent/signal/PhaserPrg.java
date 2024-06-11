package com.concurrent.signal;

import com.util.base.RandomUtil;

import java.util.concurrent.Phaser;

//Phaser是一个阶段协同器类，用于协调多个线程的执行。线程组可以在不同的
//点上进行会合，而不是仅在一个固定的屏障处。Phaser允许在同步过程中
//动态地调整参与线程的数量。线程可以在任何阶段加入或退出。
public class PhaserPrg {

    public static void main(String[] args) {
        Phaser phaser = new Phaser();

        Runnable task = () -> {
            try {
                //注册当前线程为Phaser的参与者
                System.out.println(Thread.currentThread().getName() +" "+phaser.getRegisteredParties());

                Thread.sleep(RandomUtil.nextRandomInt(100,1000));
                System.out.println(Thread.currentThread().getName() + " 到达part1-------------");
                phaser.arriveAndAwaitAdvance();
                System.out.println(Thread.currentThread().getName() +
                        "当前到达数："+phaser.getArrivedParties()+"，第"+phaser.getPhase()+"阶段");

                System.out.println(phaser.isTerminated());
                Thread.sleep(RandomUtil.nextRandomInt(100,1000));
                System.out.println(Thread.currentThread().getName() + " 到达part2..............");
                // 等待其他线程到达第2阶段
                phaser.arriveAndAwaitAdvance();
                System.out.println(Thread.currentThread().getName() +
                        "当前到达数："+phaser.getArrivedParties()+"，第"+phaser.getPhase()+"阶段");

                Thread.sleep(RandomUtil.nextRandomInt(100,1000));
                System.out.println(Thread.currentThread().getName() + " 到达part3..............");
                // 等待其他线程到达第3阶段
                phaser.arriveAndAwaitAdvance();
                System.out.println(Thread.currentThread().getName() +
                        "当前到达数："+phaser.getArrivedParties()+"，第"+phaser.getPhase()+"阶段");

                Thread.sleep(RandomUtil.nextRandomInt(100,1000));
                System.out.println(Thread.currentThread().getName() + " 到达part4..............");
                // 等待其他线程到达第4阶段
                phaser.arriveAndAwaitAdvance();
                System.out.println(Thread.currentThread().getName() +
                        "当前到达数："+phaser.getArrivedParties()+"，第"+phaser.getPhase()+"阶段");

                Thread.sleep(RandomUtil.nextRandomInt(100,1000));
                System.out.println(Thread.currentThread().getName() + " 到达part5..............");
                // 等待其他线程到达第5阶段
                phaser.arriveAndAwaitAdvance();
                System.out.println(Thread.currentThread().getName() +
                        "当前到达数："+phaser.getArrivedParties()+"，第"+phaser.getPhase()+"阶段");
            } catch (Exception e) {
                e.printStackTrace(System.out);
            } finally {
                //最终注销参与者
                phaser.arriveAndDeregister();
                System.out.println(Thread.currentThread().getName() + " 任务完成 "+
                        phaser.getRegisteredParties()+" "+phaser.isTerminated());
            }
        };

        Thread thread1 = new Thread(task, "线程1");
        Thread thread2 = new Thread(task, "线程2");
        Thread thread3 = new Thread(task, "线程3");
        //参与则注册
        phaser.bulkRegister(3);
        thread1.start();
        thread2.start();
        thread3.start();
    }
}