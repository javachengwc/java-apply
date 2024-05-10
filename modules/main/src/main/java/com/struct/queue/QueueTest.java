package com.struct.queue;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

//队列就是一个先入先出（FIFO）的数据结构
//add        增加一个元索                     如果队列已满，则抛出一个IIIegaISlabEepeplian异常
//remove     移除并返回队列头部的元素         如果队列为空，则抛出一个NoSuchElementException异常
//element    返回队列头部的元素               如果队列为空，则抛出一个NoSuchElementException异常
//offer      添加一个元素并返回true           如果队列已满，则返回false
//poll       移除并返问队列头部的元素         如果队列为空，则返回null
//peek       返回队列头部的元素               如果队列为空，则返回null
//put        添加一个元素                     如果队列满，则阻塞
//take       移除并返回队列头部的元素         如果队列为空，则阻塞
//队列只允许在表的前端（front）进行删除操作，而在表的后端（rear）进行插入操作
//Queue是单向队列，只能在一端插入数据，另一端删除数据
//Queue通常应用是广度优先算法BFS
public class QueueTest {

    public static void main(String [] args ) {
        Queue<Integer> queue = new ArrayBlockingQueue<Integer>(3);
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);
        queue.offer(4);
        System.out.println("queue="+queue+",length="+queue.size());
        Integer a =queue.poll();
        System.out.println(a);
        System.out.println("after poll,queue="+queue+",length="+queue.size());
        Integer b =queue.peek();
        System.out.println(b);
        System.out.println("after peek,queue="+queue+",length="+queue.size());
    }
}
