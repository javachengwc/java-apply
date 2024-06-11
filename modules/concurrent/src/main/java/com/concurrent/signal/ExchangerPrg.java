package com.concurrent.signal;

import java.util.concurrent.Exchanger;

//Exchanger（交换者）是一个用于两个线程间的数据交换的类。
//它提供一个同步点，在这个同步点两个线程可以交换彼此的数据。
//两个线程通过exchange方法交换数据，如果第一个线程先执行exchange方法，它会一直等待第二个线程也执行exchange，
//当两个线程都到达同步点时，两个线程就可以交换数据，将本线程的数据传递给对方。
public class ExchangerPrg {

    public static void main(String [] args) {
        //exchanger用于交换数据
        Exchanger<String> exchanger = new Exchanger<>();
        Thread producer = new Thread(() -> {
            try {
                String data = "product data";
                String exchangeData = exchanger.exchange(data);
                System.out.println(Thread.currentThread().getName()+" doing.......");
                System.out.println("producer received: " + exchangeData);
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        });
        Thread consumer = new Thread(() -> {
            try {
                String data = "consume data";
                Thread.sleep(100);
                System.out.println(Thread.currentThread().getName()+" sleep end.......");
                String exchangeData = exchanger.exchange(data);
                System.out.println("consumer received: " + exchangeData);
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        });
        producer.start();
        consumer.start();
    }
}
