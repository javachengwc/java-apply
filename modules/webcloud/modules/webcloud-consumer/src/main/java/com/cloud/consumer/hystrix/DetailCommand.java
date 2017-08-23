package com.cloud.consumer.hystrix;

import com.netflix.hystrix.*;
import com.util.base.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DetailCommand extends HystrixCommand<String> {

    private static Logger logger= LoggerFactory.getLogger(DetailCommand.class);

    private String name;

    public DetailCommand(String name) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("DetailGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("DetailKey"))
                //设置执行隔离代码的线程池，此线程池默认10个线程，
                //如果其他HystrixCommand指定的线程池key也是此key,那它运行的线程池跟此Command是同一个线程池
                //如果多个Command都没指定线程池key,但都是同一个GroupKey，那它们运行的线程池也是同一个线程池,不管CommandKey相不相同
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("DetailPool"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(500)));///隔离代码执行超时时间设为500毫秒
        this.name=name;
    }

    @Override
    protected String run() {
        logger.info("["+Thread.currentThread().getName()+"] ExampleCommand run start......");
        return name+" success";
    }

    @Override
    protected String getFallback() {
        logger.info("["+Thread.currentThread().getName()+"] ExampleCommand getFallback start......");
        return name+" fallback";
    }

    public static void main(String[] args) throws Exception{

        DetailCommand detailCommand = new DetailCommand("haha");
        //使用execute()执行同步调用
        String result = detailCommand.execute();
        System.out.println("result=" + result);

        DetailCommand detailCommand2 = new DetailCommand("haha2");
        String result2 = detailCommand2.execute();
        System.out.println("result2=" + result2);

        //这里通过jstack pid打印线程信息，会发现ExampleCommand,DetailCommand是在不同的线程池执行的，每个线程池默认大小是10个线程，
        //如果ExampleCommand与DetailCommand设置同样的ThreadPoolKey,则运行的线程池是同一个线程池
        int i=0;
        while(i<100) {
            ExampleCommand exampleCommand = new ExampleCommand(false);
            String result3 = exampleCommand.execute();
            System.out.println("result3=" + result3);
            i++;
        }

        ThreadUtil.sleep(5000000l);
    }
}