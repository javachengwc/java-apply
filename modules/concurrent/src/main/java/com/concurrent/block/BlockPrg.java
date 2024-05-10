package com.concurrent.block;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * BlockingQueue 阻塞队列
 *   put()    ---添加一个元素 如果队列满，则阻塞
 *   take()   ---移除并返回队列头部的元素 如果队列为空，则阻塞
 *
 *   offer()  ---添加一个元素并返回true 如果队列已满，则返回false
 *   poll()   ---移除并返问队列头部的元素 如果队列为空，则返回null
 *
 *   peek()   ---返回队列头部的元素 如果队列为空，则返回null
 *   element()---返回队列头部的元素 如果队列为空，则抛出一个NoSuchElementException异常
 *   add()    ---增加一个元索 如果队列已满，则抛出一个IIIegaISlabEepeplian异常
 *   remove() ---移除并返回队列头部的元素 如果队列为空，则抛出一个NoSuchElementException异常
 *
 *   element(),add(),remove()一般不被使用
 *
 * BlockingDeque 阻塞队列
 */
public class BlockPrg {

    public static void main(String ...args) throws Exception
    {
        Thread t1 = new Thread(){
            public void run()
            {
                try {
                    dequeDo();
                }catch(Exception e)
                {
                    e.printStackTrace(System.out);
                }
            }
        };
        t1.start();

        Thread t2 = new Thread(){
            public void run()
            {
                try {
                    queueDo();
                }catch(Exception e)
                {
                    e.printStackTrace(System.out);
                }
            }
        };
        t2.start();
    }

    public static void dequeDo() throws InterruptedException {

        BlockingDeque bDeque = new LinkedBlockingDeque(20);
        for (int i = 0; i < 30; i++) {

            //将指定元素添加到此阻塞栈中，如果没有可用空间，将一直等待（如果有必要）。
            bDeque.putFirst(i);
            System.out.println("向阻塞栈中添加了元素:" + i);
        }

        System.out.println("程序到此运行结束，即将退出----");
    }

    public static void queueDo() throws InterruptedException {
        BlockingQueue bqueue = new ArrayBlockingQueue(20);
        for (int i = 0; i < 30; i++) {
            //将指定元素添加到此队列中，如果没有可用空间，将一直等待（如果有必要）。
            bqueue.put(i);
            System.out.println("向阻塞队列中添加了元素:" + i);
        }

        System.out.println("程序到此运行结束，即将退出----");
    }

}
