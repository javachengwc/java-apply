package com.concurrent.thread;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

/**
 * ForkJoinPool可以在有限的线程数下来完成非常多的具有父子关系的任务
 * ForkJoinPool由ForkJoinTask数组和ForkJoinWorkerThread数组组成，
 * ForkJoinTask数组负责将存放程序提交给ForkJoinPool，而ForkJoinWorkerThread负责执行这些任务。
 * 任务分割出的子任务会添加到当前工作线程所维护的双端队列中，进入队列的头部。
 * 当一个工作线程的队列里暂时没有任务时，它会随机从其他工作线程的队列的尾部获取一个任务(工作窃取算法)。
 * 每一个ForkJoinWorkerThread线程都具有一个独立的任务等待队列（work queue）
 * workQueue任务队列用于存储在本线程中被拆分的若干子任务。
 */
public class ForkJoinPoolPrg {

    //临界值
    public static final Long critical = 100000L;

    //实现ForkJoin需要继承RecursiveTask或者RecursiveAction，
    //RecursiveTask是有返回值的，RecursiveAction 则没有。
    public static class ForkJoinWork extends RecursiveTask<Long> {
        private Long start;//起始值
        private Long end;//结束值

        public ForkJoinWork(Long start, Long end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            //判断是否是拆分完毕
            Long lenth = end - start;
            if (lenth <= critical) {
                //如果拆分完毕就相加
                Long sum = 0L;
                for (Long i = start; i <= end; i++) {
                    sum += i;
                }
                return sum;
            } else {
                //没有拆分完毕就开始拆分
                //计算的两个值的中间值
                Long middle = (end + start) / 2;
                ForkJoinWork right = new ForkJoinWork(start, middle);
                //拆分，并压入线程队列
                right.fork();
                ForkJoinWork left = new ForkJoinWork(middle + 1, end);
                //拆分
                left.fork();

                //合并
                return right.join() + left.join();
            }
        }
    }

    public static void main(String[] args) {
        Long x = 0L;
        Long y = 1000000000L;

        //并行流计算
        streamCompute(x,y);
        //普通计算
        generalCompute(x,y);

        long now = System.currentTimeMillis();
        //实现ForkJoin需要有ForkJoinPool的支持
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Long> task = new ForkJoinWork(x, y);
        Long result = forkJoinPool.invoke(task);
        long endMs = System.currentTimeMillis();
        System.out.println("result = " + result + " ForkJoin计算耗时time: " + (endMs - now) +"ms");
    }

    //普通计算
    private static void generalCompute(Long x,Long y) {
        long now = System.currentTimeMillis();
        Long sum=0L;
        for (Long i = x; i <= y; i++) {
            sum += i;
        }
        long endMs = System.currentTimeMillis();
        System.out.println("sum = " + sum + " 普通计算耗时time: " + (endMs - now));
    }

    //java8并行流计算
    //java8提供了一个并行流来实现ForkJoin实现的功能。
    //java8将并行流进行了优化,并行流比自己实现的ForkJoin还要快。
    private static void streamCompute(Long x,Long y) {
        long now = System.currentTimeMillis();
        //Stream API可以声明性的通过parallel()与sequential()在并行流与串行流中随意切换
        long reduce = LongStream.rangeClosed(x, y).parallel().reduce(0, Long::sum);
        long endMs = System.currentTimeMillis();
        System.out.println("reduce = " + reduce + " 并行流计算耗时time: " + (endMs - now));
    }
}


