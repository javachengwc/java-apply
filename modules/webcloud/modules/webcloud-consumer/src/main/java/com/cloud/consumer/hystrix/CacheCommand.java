package com.cloud.consumer.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 缓存hystrixCommand
 */
public class CacheCommand extends HystrixCommand<String> {

    private static Logger logger= LoggerFactory.getLogger(CacheCommand.class);

    private String value;

    public CacheCommand(String value) {
        super(HystrixCommandGroupKey.Factory.asKey("CacheCommand"));
        this.value = value;
    }
    @Override
    protected String run() {
        logger.info("[" + Thread.currentThread().getName() + "] CacheCommand run start......");
        return "value=" + value;
    }
    //重写getCacheKey方法,区分不同请求
    @Override
    protected String getCacheKey() {
        return value;
    }

    public static void main(String[] args){
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            CacheCommand b1 = new CacheCommand("b");
            CacheCommand b2 = new CacheCommand("b");
            b1.execute();
            System.out.println(b1.isResponseFromCache());
            b2.execute();
            System.out.println(b2.isResponseFromCache());
        } finally {
            context.shutdown();
        }

        context = HystrixRequestContext.initializeContext();
        try {
            CacheCommand b3 = new CacheCommand("b");
            b3.execute();
            System.out.println(b3.isResponseFromCache());
        } finally {
            context.shutdown();
        }
    }
}