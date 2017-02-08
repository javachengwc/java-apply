package com.concurrent.signal;

import com.util.base.ThreadUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Semaphore  用于控制某资源同时被访问的个数,可以用着对并发数量的一个控制
 *    acquire(x) 需要得到许可，如果莫有可用的许可，阻塞，直到别的线程释放许可
 */
public class SemaphorePrg {

    public static void main(String[] args) {
        MyPool myPool = new MyPool(20);
        //创建线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        MyThread t1 = new MyThread("任务A", myPool, 3);
        MyThread t2 = new MyThread("任务B", myPool, 12);
        MyThread t3 = new MyThread("任务C", myPool, 10);
        //在线程池中执行任务
        threadPool.execute(t1);
        threadPool.execute(t2);
        threadPool.execute(t3);
        //关闭池
        threadPool.shutdown();
    }
}

/**
 * 一个池
 */
class MyPool {
    private Semaphore sp;     //池相关的信号量

    /**
     * 池的大小，这个大小会传递给信号量
     * @param size 池的大小
     */
    MyPool(int size) {
        this.sp = new Semaphore(size);
    }

    public Semaphore getSp() {
        return sp;
    }

    public void setSp(Semaphore sp) {
        this.sp = sp;
    }
}


class MyThread extends Thread {
    private String threadname;            //线程的名称
    private MyPool pool;                  //自定义池
    private int x;                        //申请信号量的大小

    MyThread(String threadname, MyPool pool, int x) {
        this.threadname = threadname;
        this.pool = pool;
        this.x = x;
    }

    public void run() {
        try {
            //从此信号量获取给定数目的许可
            pool.getSp().acquire(x);
            System.out.println(threadname + "成功获取了" + x + "个许可！");
            if(threadname=="任务B")
            {
                ThreadUtil.sleep(5000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //释放给定数目的许可，将其返回到信号量。
            pool.getSp().release(x);
            System.out.println(threadname + "释放了" + x + "个许可！");
        }
    }
}
