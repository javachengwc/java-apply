package com.struct.queue;

public class SelfQueue {

    //队列元素
    private Object[] array;
    //队列长度
    private int maxSize;
    //队头下标
    private int front;
    //队尾下标
    private int tail;

    //队列中元素的实际数目
    private int count;

    public SelfQueue(int s){
        maxSize = s;
        array = new Object[maxSize];
        front = 0;
        tail = -1;
        count = 0;
    }

    //新增数据
    //队列中新增一个数据时，队尾下标会向上移动。移除数据项时，队头下标也向上移动。当队尾下标移动到数组的最上端，
    //为了避免不能插入新的数据，让队尾绕回到数组开始的位置，这称为“循环队列”
    public boolean offer(int value){
        if(isFull()){
            System.out.println("队列已满");
            return false;
        }else{
            //如果队列尾部指向顶了，循环回来
            if(tail == maxSize -1){
                tail = -1;
            }
            //队尾下标加1，然后在队尾下标处写入新的数据
            tail++;
            array[tail] = value;
            //增加实际数量
            count++;
            return true;
        }
    }

    //移除数据
    public Object poll(){
        if(isEmpty()) {
            System.out.println("队列为空");
            return null;
        }
        Object value = array[front];
        array[front] = null;
        front++;
        if(front == maxSize){
            front = 0;
        }
        count--;
        return value;
    }

    //查看对头数据
    public Object peekFront(){
        return array[front];
    }


    //判断队列是否满
    public boolean isFull(){
        return count == maxSize;
    }

    //判断队列是否空
    public boolean isEmpty(){
        return count ==0;
    }

    //返回队列的大小
    public int getSize(){
        return count;
    }
}
