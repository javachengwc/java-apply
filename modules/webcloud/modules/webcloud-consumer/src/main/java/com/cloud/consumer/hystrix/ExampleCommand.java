package com.cloud.consumer.hystrix;

import com.netflix.hystrix.*;
import com.util.base.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ExampleCommand extends HystrixCommand<String> {

    private static Logger logger= LoggerFactory.getLogger(ExampleCommand.class);

    private Boolean throwException;

    public ExampleCommand(Boolean throwException) {
        //指定命令组(CommandGroup),只是一个业务分组
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        //super(setter());
        this.throwException=throwException;
    }

    private static Setter setter() {
       //服务组
        HystrixCommandGroupKey groupKey = HystrixCommandGroupKey.Factory.asKey("ExampleGroup");
       //服务key
        HystrixCommandKey commandKey =HystrixCommandKey.Factory.asKey("ExampleKey");
       //线程池key
        HystrixThreadPoolKey threadPoolKey = HystrixThreadPoolKey.Factory.asKey("ExamplePool");
       //线程池配置
        HystrixThreadPoolProperties.Setter threadPoolProperties =HystrixThreadPoolProperties.Setter()
                .withCoreSize(5)
                .withMaxQueueSize(Integer.MAX_VALUE)
                .withQueueSizeRejectionThreshold(5);
                //queueSizeRejectionThreshold限定当前队列大小，即实际队列大小
       //命令属性配置
        HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter()
                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD);
        return HystrixCommand.Setter
                .withGroupKey(groupKey)
                .andCommandKey(commandKey)
                .andThreadPoolKey(threadPoolKey)
                .andThreadPoolPropertiesDefaults(threadPoolProperties)
                .andCommandPropertiesDefaults(commandProperties);
    }

    @Override
    protected String run() {
        logger.info("["+Thread.currentThread().getName()+"] ExampleCommand run start......");
        //run方法不能超过command定义的超时时间,默认:1秒，如果超时则调用fallback
        ThreadUtil.sleep(100l);
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
        ThreadUtil.sleep(2000l);
        String result3 = future.get();
        System.out.println("result3=" + result3);

        //注册观察者事件拦截
        Observable<String> fs = new ExampleCommand(false).observe();
        //注册结果回调事件
        fs.subscribe(new Action1<String>() {
            public void call(String result) {
                logger.info("["+Thread.currentThread().getName()+"]ExampleCommand action call....result="+result);
            }
        });
        //注册完整执行生命周期事件
        fs.subscribe(new Observer<String>() {
            @Override
            public void onError(Throwable e) {
                //异常时回调
                logger.info("["+Thread.currentThread().getName()+"]Observer onError " + e.getMessage());
            }
            @Override
            public void onNext(String result) {
                //获取结果后回调
                logger.info("["+Thread.currentThread().getName()+"]Observer onNext " + result);
            }
            @Override
            public void onCompleted() {
                //onNext/onError完成之后最后回调
                logger.info("["+Thread.currentThread().getName()+"]Observer execute onCompleted");
            }
        });

//        ExecutorService executorService= Executors.newFixedThreadPool(5);
//        int i=0;
//        while(i<1000) {
//            executorService.execute(new Runnable() {
//                @Override
//                public void run() {
//                    logger.info("ExecutorService thread-->"+Thread.currentThread().getName());
//                    ExampleCommand exampleCommandK = new ExampleCommand(false);
//                    String rt = exampleCommandK.execute();
//                    System.out.println("rt=" + rt);
//                }
//            });
//            i++;
//        }
//        executorService.shutdown();
    }
}