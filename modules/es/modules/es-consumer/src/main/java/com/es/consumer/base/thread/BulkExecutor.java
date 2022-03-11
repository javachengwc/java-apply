package com.es.consumer.base.thread;

import com.es.consumer.base.config.SettingConfig;
import com.es.consumer.es.EsService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * es bulk操作线程池
 */
public class BulkExecutor {

    private static Logger logger = LoggerFactory.getLogger(BulkExecutor.class);

    //批量数据map
    private static Map<String,String> bulkMap = new ConcurrentHashMap<String,String>(100);

    private static int threadQueueSize= 300;

    private static BulkExecutor inst= new BulkExecutor();

    public static BulkExecutor getInstance( ) {
        return inst;
    }

    //线程池
    private ThreadPoolExecutor executors = null;

    private BulkExecutor() {
        init();
    }

    public void init() {
        logger.info("BulkExecutor init start.............");
        executors = new ThreadPoolExecutor(8,8,10L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(threadQueueSize),
                new ThreadFactory() {
                    final AtomicInteger poolNumber = new AtomicInteger(1);
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "BulkExecutor-thread-" + poolNumber.getAndIncrement());
                    }
                },
                new ThreadPoolExecutor.CallerRunsPolicy());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                while (!executors.awaitTermination(10, TimeUnit.SECONDS)) {
                    String threadInfo = this.getExecutorStatus();
                    logger.info("BulkExecutor thread doing 执行中,线程池信息:" + threadInfo);
                }
            } catch (Exception e) {
                logger.info("BulkExecutor shutdownHook error,", e);
            }
        }));
        logger.info("BulkExecutor init end.............");
    }

    public void bulkInsert(EsService esService,String data ,String index, String type, String id) {
        boolean autoId= StringUtils.isBlank(id)?true:false;
        String key = StringUtils.isBlank(id)? UUID.randomUUID().toString():id;
        bulkMap.put(key,data);
        if(bulkMap.size()>= SettingConfig.bulkCount) {
            final Map<String, String> map = new HashMap<String,String>();
            synchronized (BulkExecutor.class) {
                if (bulkMap.size() >= SettingConfig.bulkCount) {
                    map.putAll(bulkMap);
                    bulkMap.clear();
                }
            }
            if(map.size()>0) {
                try{
                    executors.submit(new Runnable() {
                        @Override
                        public void run() {
                            esService.bulkInsert(map, index ,type, autoId);
                        }
                    });
                }catch (Exception e){
                    logger.error(String.format("BulkExecutor bulkInsert error,type=%s", type), e);
                }
            }
        }
    }

    public boolean threadFull() {
        int queueTaskSize = executors.getQueue().size();
        int activeThreadCount = executors.getActiveCount();
        int maxThreadCount = executors.getMaximumPoolSize();
        if(activeThreadCount>=maxThreadCount && queueTaskSize>=threadQueueSize-3) {
            return true;
        }
        return false;
    }

    public String getExecutorStatus() {
        long taskCount = executors.getTaskCount();
        long completedCount = executors.getCompletedTaskCount();
        int queueTaskSize = executors.getQueue().size();
        int activeThreadCount = executors.getActiveCount();
        String result = "BulkExecutor.executors 运行状态:";
        result+="任务总数:"+taskCount+",完成任务数:"+completedCount+",队列中任务数:"+queueTaskSize+",活动线程数:"+activeThreadCount;
        return result;
    }
}
