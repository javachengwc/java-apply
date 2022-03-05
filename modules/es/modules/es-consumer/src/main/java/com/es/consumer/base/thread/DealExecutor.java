package com.es.consumer.base.thread;

import com.es.consumer.base.config.SettingConfig;
import com.es.consumer.es.EsService;
import com.es.consumer.es.EsWriteModeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 处理线程池
 */
public class DealExecutor {

    private static Logger logger = LoggerFactory.getLogger(DealExecutor.class);

    //信息处理es模式
    public static EsWriteModeEnum writeMode= EsWriteModeEnum.SINGLE;

    private static DealExecutor inst= new DealExecutor();

    public static DealExecutor getInstance( ) {
        return inst;
    }

    //线程池
    private ThreadPoolExecutor executors = null;

    private DealExecutor() {
        init();
    }

    public void init() {
        logger.info("DealExecutor init start.............");
        executors = new ThreadPoolExecutor(8,8,10L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(10000),
                new ThreadFactory() {
                    final AtomicInteger poolNumber = new AtomicInteger(1);
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "DealExecutor-thread-" + poolNumber.getAndIncrement());
                    }
                },
                new ThreadPoolExecutor.CallerRunsPolicy());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                while (!executors.awaitTermination(10, TimeUnit.SECONDS)) {
                    String threadInfo = this.getExecutorStatus();
                    logger.info("DealExecutor thread doing 执行中,线程池信息:" + threadInfo);
                }
            } catch (Exception e) {
                logger.info("DealExecutor shutdownHook error,", e);
            }
        }));
        logger.info("DealExecutor init end.............");
    }

    public void insert(EsService esService, String data, String index, String type, String id) {
        executors.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    if(EsWriteModeEnum.BULK== DealExecutor.writeMode) {
                        //批量
                        BulkExecutor.getInstance().bulkInsert(esService,data, index, type,id);
                    }else if(EsWriteModeEnum.HALF==DealExecutor.writeMode) {
                        //半批量
                        if(BulkExecutor.getInstance().threadFull()) {
                            singleInsert(esService,data,index,type,id);
                        }else {
                            BulkExecutor.getInstance().bulkInsert(esService,data, index, type,id);
                        }
                    }else{
                        //单个
                        singleInsert(esService,data,index,type,id);
                    }
                } catch (Exception e) {
                    logger.error("DealExecutor executors run error.cur thread name={}", Thread.currentThread().getName(), e);
                }
            }
        });
    }

    public void singleInsert(EsService esService,String data,String index,String type,String id) {
        long waitTime = SettingConfig.resultWaitTime;
        if(waitTime<=0) {
            esService.insert(data, index, type, id);
        }else {
            esService.insert(data, index, type, id, waitTime);
        }
    }

    public String getExecutorStatus() {
        long taskCount = executors.getTaskCount();
        long completedCount = executors.getCompletedTaskCount();
        int queueTaskSize = executors.getQueue().size();
        int activeThreadCount = executors.getActiveCount();
        String result = "DealExecutor.executors 运行状态:";
        result+="任务总数:"+taskCount+",完成任务数:"+completedCount+",队列中任务数:"+queueTaskSize+",活动线程数:"+activeThreadCount;
        return result;
    }
}
