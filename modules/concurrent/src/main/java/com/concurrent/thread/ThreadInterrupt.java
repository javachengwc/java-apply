package com.concurrent.thread;

/**
 * Thread.interrupt()方法不会中断一个正在运行的线程。
 * 这一方法实际上完成的是，在线程抛出一个中断信号，这样阻塞的线程就得以退出阻塞的状态。
 * 更确切的说，如果线程被Object.wait, Thread.join和Thread.sleep三种方法之一阻塞，那么，它将接收到一个中断异常（InterruptedException），从而提早地终结被阻塞状态。
 */
public class ThreadInterrupt extends Thread {

    private boolean stop = false;

    public static void main( String args[] ) throws Exception {

        ThreadInterrupt thread = new ThreadInterrupt();
        System.out.println("Starting thread...");
        thread.start();

        Thread.sleep(3000);
        System.out.println( "Asking thread to stop..." );
        thread.interrupt();

        Thread.sleep( 3000 );
        System.out.println( "Stopping application..." );
    }

    public void run() {
        while ( !stop ) {
            System.out.println( "Thread running..." );
            try {
                Thread.sleep( 1000 );
            } catch ( InterruptedException e ) {
                System.out.println( "Thread interrupted..." );
                stop=true;
            }
        }
        System.out.println( "Thread exiting..." );
    }
}
