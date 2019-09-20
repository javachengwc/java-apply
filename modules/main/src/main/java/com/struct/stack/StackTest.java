package com.struct.stack;

import java.util.Stack;

//栈是一种特殊的线性表，仅能在线性表的一端操作，栈顶允许操作，栈底不允许操作。
//栈的特点是：先进后出，或者说是后进先出
//Stack实现了List接口，也实现了Collection接口
//Stack是线程安全的，push(),pop()都是同步方法
public class StackTest {

    public static void main(String [] args ) {
        Stack<Integer> stack = new Stack<Integer>();
        for(int i=0;i<10;i++) {
            stack.push(i);
        }
        int size = stack.size();
        System.out.println("stack size: "+size);
        boolean isEmpty =stack.empty();
        System.out.println("stack isEmpty: "+isEmpty);
        for(int i=0;i<11;i++) {
            isEmpty =stack.empty();
            if(!isEmpty) {
                Integer rt = stack.pop();
                System.out.println("第[" + (i + 1) + "]= " + rt);
            }else {
                System.out.println("第[" + (i + 1) + "],isEmpty=" + isEmpty);
            }
        }
    }
}
