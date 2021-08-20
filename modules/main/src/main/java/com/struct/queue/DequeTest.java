package com.struct.queue;

import java.util.ArrayDeque;
import java.util.Deque;

//Deque是双向队列，每一端都可以进行插入数据和删除数据操作。
public class DequeTest {

    public static void main(String [] args) {
        Deque<Integer> deque = new ArrayDeque<Integer>();
        deque.offer(1);
        deque.offer(2);
        deque.offer(3);
        deque.offerFirst(0);
        System.out.println("deque="+deque+",length="+deque.size());
        Integer a=deque.pollLast();
        System.out.println(a);
        System.out.println("after pollLast,length="+deque.size());
        deque.push(4);//放到first
        deque.push(5);
        deque.push(6);
        System.out.println("deque="+deque+",length="+deque.size());
        Integer b=deque.poll();
        System.out.println(b);
        Integer c = deque.pop();
        System.out.println(c);
        System.out.println("deque="+deque+",length="+deque.size());

    }
}
