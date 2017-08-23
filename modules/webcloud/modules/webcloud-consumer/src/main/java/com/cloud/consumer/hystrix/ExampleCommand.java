package com.cloud.consumer.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.util.base.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ExampleCommand extends HystrixCommand<String> {

    private static Logger logger= LoggerFactory.getLogger(ExampleCommand.class);

    private Boolean throwException;

    public ExampleCommand(Boolean throwException) {
        //指定命令组名(CommandGroup)
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.throwException=throwException;
    }

    @Override
    protected String run() {
        logger.info("["+Thread.currentThread().getName()+"] ExampleCommand run start......");
        //run方法不能超过command定义的超时时间,默认:1秒，如果超时则调用fallback
        ThreadUtil.sleep(5000l);
        if(throwException) {
            throw new RuntimeException("ExampleCommand run exception");
        }
        return "success";
    }

    @Override
    protected String getFallback() {
        logger.info("["+Thread.currentThread().getName()+"] ExampleCommand getFallback start......");
        //fallback方法暂时没超时限制
        return "fallback";
    }

    public static void main(String[] args) throws Exception{

        ExampleCommand exampleCommand = new ExampleCommand(false);
        //使用execute()执行同步调用
        String result = exampleCommand.execute();
        System.out.println("result=" + result);

        //每个Command对象只能调用一次,不可以重复调用,
        //重复调用报异常:This instance can only be executed once. Please instantiate a new instance.
//        String result2 = exampleCommand.execute();
//        System.out.println("result2=" + result2);

        ExampleCommand  exampleCommand3= new ExampleCommand(true);
        //异步调用,可自由控制获取结果时机
        Future<String> future = exampleCommand3.queue();
        String result3 = future.get();
        System.out.println("result3=" + result3);
    }
}