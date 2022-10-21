package com.concurrent.thread;

import com.util.base.ThreadUtil;

public class ThreadSeq {

    private static int doThread=1;

    //多个线程顺序执行
    public static void main(String [] args) {

        for(int i=0;i<5;i++) {
            final int cur = i+1;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(cur!=doThread) {
                        ThreadUtil.sleep(1L);
                    }
                    System.out.println(cur);
                    doThread++;
                }
            });
            t.start();
        }
    }
}
