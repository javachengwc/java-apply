package com.concurrent.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 存取款
 * Condition中的await()方法相当于Object的wait()方法，Condition中的signal()方法相当于Object的notify()方法，Condition中的signalAll()相当于Object的notifyAll()方法。
 * 不同的是，Object中的这些方法是和同步锁捆绑使用的；而Condition是需要与互斥锁/共享锁捆绑使用的。
 * Condition强大的地方在于：能够更加精细的控制多线程的休眠与唤醒。
 * 对于同一个锁，可以创建多个Condition，在不同的情况下使用不同的Condition。
 * 例如，假如多线程读/写同一个缓冲区：当向缓冲区中写入数据之后，唤醒"读线程"；当从缓冲区读出数据之后，唤醒"写线程"；并且当缓冲区满的时候，"写线程"需要等待；当缓冲区为空时，"读线程"需要等待。
 * 如果采用Object类中的wait(), notify(), notifyAll()实现该缓冲区，当向缓冲区写入数据之后需要唤醒"读线程"时，不可能通过notify()或notifyAll()明确的指定唤醒"读线程"，
 * 而只能通过notifyAll唤醒所有线程(但是notifyAll无法区分唤醒的线程是读线程，还是写线程)。但是，通过Condition，就能明确的指定唤醒读线程。
 */
public class AccessMoney {
    public static void main(String[] args) {

        //创建并发访问的账户
        MyCount myCount = new MyCount("95599200901215522", 0);

        //创建一个线程池
        ExecutorService pool = Executors.newFixedThreadPool(2);
        Thread t1 = new SaveThread("张三", myCount, 2000);
        Thread t7 = new DrawThread("老牛", myCount, 3300);
        Thread t2 = new SaveThread("李四", myCount, 3600);
        Thread t3 = new DrawThread("王五", myCount, 2700);
        Thread t4 = new SaveThread("老张", myCount, 600);
        Thread t5 = new DrawThread("老牛", myCount, 1300);
        Thread t6 = new DrawThread("胖子", myCount, 800);

        //执行各个线程
        pool.execute(t7);
        pool.execute(t1);
        pool.execute(t2);
        pool.execute(t3);
        pool.execute(t4);
        pool.execute(t5);
        pool.execute(t6);

        //关闭线程池
        pool.shutdown();
    }
}

/**
 * 存款线程类
 */
class SaveThread extends Thread {
    private String name;            //操作人
    private MyCount myCount;        //账户
    private int x;                  //存款金额

    SaveThread(String name, MyCount myCount, int x) {
        this.name = name;
        this.myCount = myCount;
        this.x = x;
    }

    public void run() {
        myCount.saving(x, name);
    }
}

/**
 * 取款线程类
 */
class DrawThread extends Thread {
    private String name;            //操作人
    private MyCount myCount;        //账户
    private int x;                  //存款金额

    DrawThread(String name, MyCount myCount, int x) {
        this.name = name;
        this.myCount = myCount;
        this.x = x;
    }

    public void run() {
        myCount.drawing(x, name);
    }
}


/**
 * 普通银行账户，不可透支
 */
class MyCount {
    private String oid;                               //账号
    private int cash;                                 //账户余额
    private Lock lock = new ReentrantLock();          //账户锁
    private Condition _save = lock.newCondition();    //存款条件
    private Condition _draw = lock.newCondition();    //取款条件

    MyCount(String oid, int cash) {
        this.oid = oid;
        this.cash = cash;
    }

    /**
     * 存款
     *
     * @param x    操作金额
     * @param name 操作人
     */
    public void saving(int x, String name) {
        lock.lock();                        //获取锁
        try {
            if (x > 0) {
                cash += x;                      //存款
                System.out.println(name + "存款" + x + "，当前余额为" + cash);
            }
            _draw.signalAll();                  //唤醒所有等待线程。
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();                      //释放锁
        }
    }

    /**
     * 取款
     *
     * @param x        操作金额
     * @param name 操作人
     */
    public void drawing(int x, String name) {
        lock.lock();                           //获取锁
        try {
            while (cash - x < 0) {
                _draw.await();                 //阻塞取款操作
            }
            cash -= x;                     //取款
            System.out.println(name + "取款" + x + "，当前余额为" + cash);
            _save.signalAll();                  //唤醒所有存款操作
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();                      //释放锁
        }
    }
}