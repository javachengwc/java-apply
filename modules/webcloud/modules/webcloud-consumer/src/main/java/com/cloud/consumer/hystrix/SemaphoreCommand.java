package com.cloud.consumer.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.util.base.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 信号量hystrixCommand
 */
public class SemaphoreCommand extends HystrixCommand<String> {

    private static Logger logger = LoggerFactory.getLogger(SemaphoreCommand.class);

    private static AtomicInteger times = new AtomicInteger(0);

    private String value;

    public SemaphoreCommand(String value) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("SemaphoreGroup"))
            //设置隔离方式为信号量隔离
            .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
            //使用信号量隔离时，命令调用最大的并发数,默认:10
            .withExecutionIsolationSemaphoreMaxConcurrentRequests(2))
        );
        this.value = value;
    }

    @Override
    protected String run() {
        times.addAndGet(1);
        logger.info("[" + Thread.currentThread().getName() + "] SemaphoreCommand run times=" + times);
        return "value=" + value;
    }

    @Override
    protected String getFallback() {
        logger.info("["+Thread.currentThread().getName()+"] SemaphoreCommand getFallback start......");
        return "-----------------fallback-----------------";
    }

    public static void main(String[] args) throws Exception{

        ExecutorService executorService=Executors.newFixedThreadPool(10);
        int i=0;
        while(i<1000) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    logger.info("ExecutorService thread-->"+Thread.currentThread().getName());
                    SemaphoreCommand shCommand = new SemaphoreCommand("haha");
                    String result = shCommand.execute();
                    System.out.println("result=" + result);
                }
            });
            i++;
        }
        executorService.shutdown();
        ThreadUtil.sleep(5000000l);
    }
}